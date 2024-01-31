<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Exemplo JSP com AJAX</title>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script>
        function requisitarNome() {
            // Fazendo uma requisição AJAX ao endpoint
            $.get("webapi/myresource", function(data) {
                // Atualizando o conteúdo do elemento <p>
                $("#resultado").text("Resultado da requisição: " + data);
            });
        }
    </script>
</head>
<body>
    <h2>Exemplo JSP com AJAX</h2>

    <form>
        <button type="button" onclick="requisitarNome()">Enviar</button>
    </form>

    <p id="resultado"></p>
</body>
</html>