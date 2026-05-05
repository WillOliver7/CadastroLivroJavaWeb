<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page contentType="text/html" pageEncoding="iso-8859-1"%>
<jsp:include page="/header.jsp"/>
<jsp:include page="/menu.jsp"/>

<div class="container-fluid">
    <!-- Page Heading -->
    <h1 class="h3 mb-2 text-gray-800">Livros</h1>
    <p class="mb-4">Formulário de Cadastro</p>

    <a class="btn btn-secondary mb-4" href="${pageContext.request.contextPath}/LivroListar">
        <i class="fas fa-undo-alt"></i>
        <strong>Voltar</strong>
    </a>
    <div class="row">
        <!-- Campos de cadastramento -->        
        <div class="col-lg-9">
            <div class="card shadow mb-4">
                <div class="card-body">
                    <div class="form-group">
                        <label>Id</label>
                        <input class="form-control" type="text" name="id" id="id" 
                               value="${livro.id}" readonly="readonly"/>
                    </div>
                    <div class="form-group">
                        <label>Nome do livro</label>
                        <input class="form-control" type="text" name="nomelivro" id="nomelivro" 
                               value="${livro.nomelivro}" size="100" maxlength="100"/>
                    </div>
                    <div class="form-group">
                        <label>Autor</label>
                        <input class="form-control" type="text" name="autor" id="autor" 
                               value="${livro.autor}" size="100" maxlength="100" 
                               required="true"/>
                    </div>
                    <div class="form-group">
                        <label>ISBN</label>
                        <input class="form-control" type="text" name="isbn" id="isbn" 
                               value="${livro.isbn}" size="11" maxlength="11"/>
                    </div>
                    <div class="form-group">
                        <div class="form-line row">
                            <div class="col-sm">
                                <label>Data de Publicação</label>
                                <input class="form-control" type="date" name="datapublicacao" id="datapublicacao" 
                                       value="${livro.datapublicacao}"/>
                            </div>
                            <div class="col-sm">
                                <label>Valor do Livro</label>
                                <input class="form-control" type="text" style="text-align:right;" 
                                           name="valorlivro" id="valorlivro" 
                                           value="<fmt:formatNumber value='${livro.valorlivro}' type='currency'/>" />
                            </div>
                        </div>
                    </div>
                    <!-- Botão de Confirmação --> 
                    <div class="form-group">
                        <button class="btn btn-success" type="submit" id="submit" onclick="validarCampos()">
                            Salvar Documento</button>
                    </div> 
                </div>
            </div>
        </div>
    </div>
</div>

                               
<script>
    $(document).ready(function () {
        console.log("entrei na ready do documento");
        $("#valorlivro").maskMoney({
            prefix: 'R$',
            suffix: '',
            allowZero: false,
            allowNegative: true,
            allowEmpty: false,
            doubleClickSelection: true,
            selectAllOnFocus: true,
            thousands: '.',
            decimal: ",",
            precision: 2,
            affixesStay: true,
            bringCareAtEndOnFocus: true
        });
        
        $('#nomelivro').focus();
    });

    function validarCampos() {
        console.log("entrei na validação de campos");
        if (document.getElementById("nomelivro").value === '') {
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: 'Verifique o nome do livro!',
                showConfirmButton: false,
                timer: 1000
            });
            $("#nomelivro").focus();
        } else if (document.getElementById("isbn").value === '') {
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: 'Verifique o ISBN!',
                showConfirmButton: false,
                timer: 1000
            });
            $("#isbn").focus();
        } else if (document.getElementById("autor").value === '') {
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: 'Verifique o autor!',
                showConfirmButton: false,
                timer: 1000
            });
            $("#autor").focus();
        } else if (document.getElementById("datapublicacao").value === '') {
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: 'Verifique a Data de Publicação!',
                showConfirmButton: false,
                timer: 1000
            });
            $("#datapublicacao").focus();
        } else if (document.getElementById("valorlivro").value === '') {
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: 'Verifique o valor do livro!',
                showConfirmButton: false,
                timer: 1000
            });
            $("#valorlivro").focus();
        } else {
            gravarDados();
        }
    }
    
    function gravarDados() {
        console.log("Gravando dados ....");
        $.ajax({
            type: 'post',
            url: 'LivroCadastrar',
            data: {
                id: $('#id').val(),
                nomelivro: $('#nomelivro').val().toUpperCase(),
                isbn: $('#isbn').unmask().val(),
                datapublicacao: $('#datapublicacao').val(),
                autor: $('#autor').val(),
                valorlivro: $('#valorlivro').val()
            },
            success: function (data) {
                console.log("resposta servlet->");
                console.log(data);
                if (data == 1) {
                    Swal.fire({
                        position: 'center',
                        icon: 'success',
                        title: 'Sucesso',
                        text: 'Livro gravado com sucesso!',
                        showConfirmButton: true,
                        timer: 3000
                    }).then(function () {
                        // RECARREGA A PÁGINA INTEIRA com formulário limpo
                        window.location.href = 'LivroNovo';
                    });
                } else if (data == 3) {
                    Swal.fire({
                        position: 'center',
                        icon: 'error',
                        title: 'CPF invalido!',
                        showConfirmButton: true,
                        timer: 5000
                    }).then(function () {
                        setTimeout(function () {
                            $('#nomelivro').focus();
                        }, 50); 
                    });
                } else if (data == 4) {
                    Swal.fire({
                        position: 'center',
                        icon: 'error',
                        title: 'CPF já cadastrado!',
                        showConfirmButton: true,
                        timer: 5000
                    }).then(function () {
                        setTimeout(function () {
                            $('#nomelivro').focus();
                        }, 50); 
                    });
                 } else if (data == 4) {
                    Swal.fire({
                        position: 'center',
                        icon: 'error',
                        title: 'CPF já cadastrado!',
                        showConfirmButton: true,
                        timer: 5000
                    }).then(function () {
                        setTimeout(function () {
                            $('#nomelivro').focus();
                        }, 50); 
                    });
                } else if (data == 0) {
                    Swal.fire({
                        position: 'center',
                        icon: 'error',
                        title: 'Retornou zero!',
                        showConfirmButton: true,
                        timer: 5000
                    }).then(function () {
                        setTimeout(function () {
                            $('#nomelivro').focus();
                        }, 50); 
                    });
                } else {
                    Swal.fire({
                        position: 'center',
                        icon: 'error',
                        title: 'Não foi possível gravar o Livro!',
                        showConfirmButton: true,
                        timer: 5000
                    }).then(function () {
                        setTimeout(function () {
                            $('#nomelivro').focus();
                        }, 50); // um pequeno atraso resolve o problema
                    });
                }
            },
            error: function () {
                Swal.fire({
                    position: 'center',
                    icon: 'error',
                    title: 'Erro de comunicação',
                    text: 'Falha na comunicação com o servidor.',
                    showConfirmButton: true,
                    timer: 5000
                }).then(function () {
                    setTimeout(function () {
                        $('#nomelivro').focus();
                    }, 50);
                });
            }
        });
    }


</script>   
<jsp:include page="/footer.jsp"/>