package service;

import java.util.Scanner;
import java.io.File;
import java.util.List;
import dao.LivroDAO;
import model.Livro;
import spark.Request;
import spark.Response;

public class LivroService {
	private LivroDAO livroDAO = new LivroDAO();
	private String form;
	private final int FORM_INSERT = 1;
	private final int FORM_DETAIL = 2;
	private final int FORM_UPDATE = 3;
	private final int FORM_ORDERBY_CODIGO = 1;
	private final int FORM_ORDERBY_TITULO = 2;
	private final int FORM_ORDERBY_AUTOR = 3;
	private final int FORM_ORDERBY_ANO = 4;
	
	
	public LivroService() {
		makeForm();
	}

	
	public void makeForm() {
		makeForm(FORM_INSERT, new Livro(), FORM_ORDERBY_TITULO);
	}

	
	public void makeForm(int orderBy) {
		makeForm(FORM_INSERT, new Livro(), orderBy);
	}

	
	public void makeForm(int tipo, Livro livro, int orderBy) {
		String nomeArquivo = "./src/main/resources/form.html";
		form = "";
		try{
			Scanner entrada = new Scanner(new File(nomeArquivo));
		    while(entrada.hasNext()){
		    	form += (entrada.nextLine() + "\n");
		    }
		    entrada.close();
		}  catch (Exception e) { System.out.println(e.getMessage()); }
		
		String umLivro = "";
		if(tipo == FORM_INSERT || tipo == FORM_UPDATE) {
			String action = "/livro/";
			String name, titulo, buttonLabel;
			if (tipo == FORM_INSERT){
				action += "insert";
				name = "Inserir Livro";
				titulo = "";
				buttonLabel = "Inserir";
			} else {
				action += "update/" + livro.getCodigo();
				name = "Atualizar Livro (Código " + livro.getCodigo() + ")";
				titulo = livro.getTitulo();
				buttonLabel = "Atualizar";
			}
			umLivro += "\t<form class=\"form--register\" action=\"" + action + "\" method=\"post\" id=\"form-add\">";
			umLivro += "\t<table width=\"80%\" align=\"center\" class=\"principal__form--inserir\">";
			umLivro += "\t\t<tr>";
			umLivro += "\t\t\t<td colspan=\"3\" align=\"center\"><font size=\"+2\"><b>" + name + "</b></font></td>";
			umLivro += "\t\t</tr>";
			umLivro += "\t\t<tr class=\"principal__form--titulos-atributos\">";
			umLivro += "\t\t\t<td>Título: <input class=\"input--register\" type=\"text\" name=\"titulo\" placeholder=\"Título do Livro\" value=\""+ titulo +"\"></td>";
			umLivro += "\t\t\t<td>Ano: <input class=\"input--register\" type=\"text\" name=\"ano\" placeholder=\"0000\" value=\""+ livro.getAno() +"\"></td>";
			umLivro += "\t\t\t<td>Autor: <input class=\"input--register\" type=\"text\" name=\"autor\" placeholder=\"Nome do Autor\" value=\""+ livro.getAutor() +"\"></td>";
			umLivro += "\t\t</tr>";
			umLivro += "\t\t<tr>";
			umLivro += "\t\t\t<td colspan=\"3\" align=\"center\"><input type=\"submit\" value=\""+ buttonLabel +"\" class=\"input--main__style input--button btn btn-danger\"></td>";
			umLivro += "\t\t</tr>";
			umLivro += "\t</table>";
			umLivro += "\t</form>";		
		} else if (tipo == FORM_DETAIL){
			umLivro += "\t<table class=\"principal__tabela--detalhes\" width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umLivro += "\t\t<tr>";
			umLivro += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>Detalhar Livro (Código " + livro.getCodigo() + ")</b></font></td>";
			umLivro += "\t\t</tr>";
			umLivro += "\t\t<tr>";
			umLivro += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umLivro += "\t\t</tr>";
			umLivro += "\t\t<tr>";
			umLivro += "\t\t\t<td>Título: "+ livro.getTitulo() +"</td>";
			umLivro += "\t\t\t<td>Ano: "+ livro.getAno() +"</td>";
			umLivro += "\t\t\t<td>Autor: "+ livro.getAutor() +"</td>";
			umLivro += "\t\t</tr>";
			umLivro += "\t\t<tr>";
			umLivro += "\t\t\t<td></td>";
			umLivro += "\t\t</tr>";
			umLivro += "\t</table>";		
		} else {
			System.out.println("ERRO! Tipo não identificado " + tipo);
		}
		form = form.replaceFirst("<!--UM-LIVRO-->", umLivro);
		
		String list = new String("<table class=\"principal__tabela--livros\" width=\"80%\" align=\"center\" bgcolor=\"#f3f3f3\">");
		list += "\n<tr><td colspan=\"7\" align=\"center\"><font size=\"+2\"><b>Relação de Livros</b></font></td></tr>\n" +
    			"\n<tr>\n" + 
        		"\t<td align=\"center\"><a href=\"/livro/list/" + FORM_ORDERBY_CODIGO + "\"><b><i class=\"fa-solid fa-sort\"></i> Código</b></a></td>\n" +
        		"\t<td align=\"center\"><a href=\"/livro/list/" + FORM_ORDERBY_TITULO + "\"><b><i class=\"fa-solid fa-sort\"></i> Título</b></a></td>\n" +
        		"\t<td align=\"center\"><a href=\"/livro/list/" + FORM_ORDERBY_AUTOR + "\"><b><i class=\"fa-solid fa-sort\"></i> Autor</b></td>\n" +
        		"\t<td><a href=\"/livro/list/" + FORM_ORDERBY_ANO + "\"><b><i class=\"fa-solid fa-sort\"></i> Ano</b></a></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Detalhar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Atualizar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Excluir</b></td>\n" +
        		"</tr>\n";
		
		List<Livro> livros;
		if (orderBy == FORM_ORDERBY_CODIGO) {               livros = livroDAO.getOrderByCodigo();
		} else if (orderBy == FORM_ORDERBY_TITULO) {		livros = livroDAO.getOrderByTitulo();
		} else if (orderBy == FORM_ORDERBY_AUTOR) {			livros = livroDAO.getOrderByAutor();
		} else if (orderBy == FORM_ORDERBY_ANO) {			livros = livroDAO.getOrderByAno();
		} else {											livros = livroDAO.get();
		}

		int i = 0;
		String bgcolor = "";
		for (Livro p : livros) {
			bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
			list += "\n<tr bgcolor=\""+ bgcolor +"\">\n" + 
            		  "\t<td width=\"150\" align=\"center\">" + p.getCodigo() + "</td>\n" +
            		  "\t<td>" + p.getTitulo() + "</td>\n" +
            		  "\t<td>" + p.getAutor() + "</td>\n" +
            		  "\t<td>" + p.getAno() + "</td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/livro/" + p.getCodigo() + "\"><i class=\"fa-solid fa-magnifying-glass\"></i></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/livro/update/" + p.getCodigo() + "\"><i class=\"fa-solid fa-pen-to-square\"></i></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/livro/delete/" + p.getCodigo() + "\"><i class=\"fa-solid fa-trash\"></i></a></td>\n" +
            		  "</tr>\n";
		}
		list += "</table>";		
		form = form.replaceFirst("<!--LISTAR-LIVRO-->", list);		
	}
	
	
	public Object insert(Request request, Response response) {
		String titulo = request.queryParams("titulo");
		int ano = Integer.parseInt(request.queryParams("ano"));
		String autor = request.queryParams("autor");
		
		String resp = "";
		
		Livro livro = new Livro(-1, titulo, autor, ano);
		
		if(livroDAO.insert(livro) == true) {
            resp = "Livro (" + titulo + ") inserido!";
            response.status(201); // 201 Created
		} else {
			resp = "Livro (" + titulo + ") não inserido!";
			response.status(404); // 404 Not found
		}
			
		makeForm();
		return form.replaceFirst("<!--MENSAGEM-->", "<p>" + resp + "</p>");
	}

	
	public Object get(Request request, Response response) {
		int codigo = Integer.parseInt(request.params(":codigo"));		
		Livro livro = (Livro) livroDAO.get(codigo);
		
		if (livro != null) {
			response.status(200); // success
			makeForm(FORM_DETAIL, livro, FORM_ORDERBY_TITULO);
        } else {
            response.status(404); // 404 Not found
            String resp = "Livro " + codigo + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<!--MENSAGEM-->", "<p>" + resp + "</p>");     
        }

		return form;
	}

	
	public Object getToUpdate(Request request, Response response) {
		int codigo = Integer.parseInt(request.params(":codigo"));		
		Livro livro = (Livro) livroDAO.get(codigo);
		
		if (livro != null) {
			response.status(200); // success
			makeForm(FORM_UPDATE, livro, FORM_ORDERBY_TITULO);
        } else {
            response.status(404); // 404 Not found
            String resp = "Livro " + codigo + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<!--MENSAGEM-->", "<p>" + resp + "</p>");     
        }

		return form;
	}
	
	
	public Object getAll(Request request, Response response) {
		int orderBy = Integer.parseInt(request.params(":orderby"));
		makeForm(orderBy);
	    response.header("Content-Type", "text/html");
	    response.header("Content-Encoding", "UTF-8");
		return form;
	}			
	
	public Object update(Request request, Response response) {
        int codigo = Integer.parseInt(request.params(":codigo"));
		Livro livro = livroDAO.get(codigo);
        String resp = "";       

        if (livro != null) {
        	livro.setTitulo(request.queryParams("titulo"));
        	livro.setAno(Integer.parseInt(request.queryParams("ano")));
        	livro.setAutor(request.queryParams("autor"));
        	livroDAO.update(livro);
        	response.status(200); // success
            resp = "Livro (Codigo " + livro.getCodigo() + ") atualizado!";
        } else {
            response.status(404); // 404 Not found
            resp = "Livro (Codigo \" + livro.getCodigo() + \") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<!--MENSAGEM-->", "<p>" + resp + "</p>");
	}

	
	public Object delete(Request request, Response response) {
        int codigo = Integer.parseInt(request.params(":codigo"));
        Livro livro = livroDAO.get(codigo);
        String resp = "";       

        if (livro != null) {
            livroDAO.delete(codigo);
            response.status(200); // success
            resp = "Livro (" + codigo + ") excluído!";
        } else {
            response.status(404); // 404 Not found
            resp = "Livro (" + codigo + ") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<!--MENSAGEM-->", "<p>" + resp + "</p>");
	}
}
