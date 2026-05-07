package br.com.cadastrolivros.dao;


import br.com.cadastrolivros.model.Livro;
import br.com.cadastrolivros.utils.SingleConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class LivroDAO implements GenericDAO {
    
    private Connection conexao;
    
    public LivroDAO() throws Exception{
        conexao = SingleConnection.getConnection();
    }

    @Override
    public Boolean cadastrar(Object objeto) {
        Livro oLivro = (Livro) objeto;
        Boolean retorno=false;
        if (oLivro.getId()== 0) {
            retorno = this.inserir(oLivro);
        }else{
            retorno = this.alterar(oLivro);
        }
        return retorno;
    }

    @Override
    public Boolean inserir(Object objeto) {
        Livro oLivro = (Livro) objeto;
        PreparedStatement stmt = null;
        String sql = "insert into livro (nomelivro,isbn, autor, datapublicacao, valorlivro) "
                + "values (?,?,?,?,?)";  
        try {
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, oLivro.getNomelivro());        
            stmt.setString(2, oLivro.getIsbn());
            stmt.setString(3, oLivro.getAutor());
            stmt.setDate(4, new java.sql.Date(oLivro.getDatapublicacao().getTime()));
            stmt.setDouble(5, oLivro.getValorlivro());
            stmt.execute();
            conexao.commit();
            return true;
        } catch (Exception ex) {
            try {
                System.out.println("Problemas ao cadastrar a Livro! Erro: "+ex.getMessage());
                ex.printStackTrace();
                conexao.rollback();
            } catch (SQLException e) {
                System.out.println("Erro:"+e.getMessage());
                e.printStackTrace();
            }
            return false;
        } 
    }

    @Override
    public Boolean alterar(Object objeto) {
        Livro oLivro = (Livro) objeto;
        PreparedStatement stmt = null;
        String sql = "update livro set nomelivro=?, isbn=?, autor=?, datapublicacao=?, valorlivro=? "
                + "where id=?";  
        try {
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, oLivro.getNomelivro());        
            stmt.setString(2, oLivro.getIsbn());
            stmt.setString(3, oLivro.getAutor());
            stmt.setDate(4, new java.sql.Date(oLivro.getDatapublicacao().getTime()));
            stmt.setDouble(5, oLivro.getValorlivro());
            stmt.setInt(6, oLivro.getId());
            stmt.execute();
            conexao.commit();
            return true;
        } catch (Exception ex) {
            try {
                System.out.println("Problemas ao alterar o Livro! Erro: "+ex.getMessage());
                ex.printStackTrace();
                conexao.rollback();
            } catch (SQLException e) {
                System.out.println("Erro:"+e.getMessage());
                e.printStackTrace();
            }
            return false;
        } 
    }

    @Override
    public Boolean excluir(int numero) {
        PreparedStatement stmt = null;
        String sql = "delete from livro where id=?";  
        try {
            stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, numero);
            stmt.execute();
            conexao.commit();
            return true;
        } catch (Exception ex) {
            try {
                System.out.println("Problemas ao excluir o Livro! Erro: "+ex.getMessage());
                ex.printStackTrace();
                conexao.rollback();
            } catch (SQLException e) {
                System.out.println("Erro:"+e.getMessage());
                e.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public Object carregar(int numero) {
        String sql = "Select * from livro where id=?";
        Livro oLivro = null;
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) 
        {
            stmt.setInt(1, numero);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                oLivro = new Livro();                
                oLivro.setId(rs.getInt("id"));
                oLivro.setNomelivro(rs.getString("nomelivro"));
                oLivro.setIsbn(rs.getString("isbn"));
                oLivro.setAutor(rs.getString("autor"));
                oLivro.setDatapublicacao(rs.getDate("datapublicacao"));
                oLivro.setValorlivro(rs.getDouble("valorlivro"));
            }
            
            return oLivro;
            
        } catch (SQLException ex) {
            System.out.println("Erro ao listar livros: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        return oLivro;
    }

    @Override
    public List<Object> listar() {
        List<Object> resultado = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "Select * from livro order by id";               
        try {
            stmt = conexao.prepareStatement(sql);
            rs=stmt.executeQuery();           
            while (rs.next()) {                
                Livro oLivro = new Livro();
                oLivro.setId(rs.getInt("id"));
                oLivro.setNomelivro(rs.getString("nomelivro"));
                oLivro.setAutor(rs.getString("autor"));
                oLivro.setIsbn(rs.getString("isbn"));
                oLivro.setValorlivro(rs.getDouble("valorlivro"));
                oLivro.setDatapublicacao(rs.getDate("datapublicacao"));
                resultado.add(oLivro);
            }
        }catch (SQLException ex) {
            System.out.println("Erro ao listar usuários: " + ex.getMessage());
            ex.printStackTrace();
        }
        return resultado;
    }
    
    public boolean isbnExiste(String isbn) {
        String sql = "SELECT COUNT(*) as quantidade_isbn FROM livro WHERE isbn = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt("quantidade_isbn") > 0)
                    return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
