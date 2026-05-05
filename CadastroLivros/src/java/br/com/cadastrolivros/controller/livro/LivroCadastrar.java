package br.com.cadastrolivros.controller.livro;

import br.com.cadastrolivros.dao.LivroDAO;
import br.com.cadastrolivros.model.Livro;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@WebServlet(name = "LivroCadastrar", urlPatterns = {"/LivroCadastrar"})
public class LivroCadastrar extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=iso-8859-1");
        try{
            
            LivroDAO dao = new LivroDAO();
            String nomelivro = request.getParameter("nomelivro");
            String isbn = request.getParameter("isbn");
            String autor = request.getParameter("autor");
            Date datapublicacao = java.sql.Date.valueOf(request.getParameter("datapublicacao"));            
            String valorlivroStr = request.getParameter("valorlivro");

            // Remove "R$", espaços e pontos de milhar, e troca a vírgula por ponto
            valorlivroStr = valorlivroStr.replace("R$", "")     // remove "R$"
                           .replace(".", "")       // remove ponto de milhar
                           .replace(",", ".")      // troca vírgula decimal por ponto
                           .trim();                // remove espaços extras

            double valorlivro = Double.parseDouble(valorlivroStr);
            
            if (nomelivro.isEmpty() || nomelivro.isBlank() || valorlivro <= 0 || 
                       isbn.isBlank() || autor.isEmpty()) {
                //verifica inconsistencias em outros atributos do cadastro
                response.getWriter().write("5");
            } else {
                //passou nas validacoes - grava dados
                Livro oLivro = new Livro();
                oLivro.setNomelivro(nomelivro);
                oLivro.setIsbn(isbn);
                oLivro.setAutor(autor);
                oLivro.setDatapublicacao(datapublicacao);
                oLivro.setValorlivro(valorlivro);

                if (dao.cadastrar(oLivro)){
                    response.getWriter().write("1");               
                } else {
                    response.getWriter().write("0");
                }
            }
        } catch (Exception ex){
            System.out.println("Problemas no Servlet ao cadastrar Livro! Erro: " + ex.getMessage());
        };
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
