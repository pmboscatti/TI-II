package app;

import static spark.Spark.*;
import service.LivroService;

public class Aplicacao {
	
	private static LivroService livroService = new LivroService();
	
    public static void main(String[] args) {
        port(6789);
        
        staticFiles.location("/public");
        
        post("/livro/insert", (request, response) -> livroService.insert(request, response));

        get("/livro/:codigo", (request, response) -> livroService.get(request, response));
        
        get("/livro/list/:orderby", (request, response) -> livroService.getAll(request, response));

        get("/livro/update/:codigo", (request, response) -> livroService.getToUpdate(request, response));
        
        post("/livro/update/:codigo", (request, response) -> livroService.update(request, response));
           
        get("/livro/delete/:codigo", (request, response) -> livroService.delete(request, response));
    }
}