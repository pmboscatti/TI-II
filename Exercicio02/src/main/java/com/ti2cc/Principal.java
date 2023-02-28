package com.ti2cc;

import java.util.List;
import java.util.Scanner;

import com.ti2cc.DAO;
import com.ti2cc.LivroDAO;
import com.ti2cc.Livro;

public class Principal {
	public static void main(String[] args) throws Exception {
		
		LivroDAO livroDAO = new LivroDAO();
		Livro livro = new Livro(11, "Angustia", "Graciliano", 1936);
		
        Scanner input = new Scanner(System.in);
		
		// Exibir o painel de opções
		exibeMenu();
		int opcao = input.nextInt();
		
		while(opcao >= 1 && opcao <=4) {
			
			if(opcao == 1) {				
				// Inserir um elemento na tabela
				System.out.println("\n\n==== Inserir Livro === ");
				
				if(livroDAO.insert(livro) == true) {
					System.out.println("Inserção com sucesso -> " + livro.toString());
				}
				
				System.out.println("\n\n==== Testando Autenticação ===");
				System.out.println("Livro (" + livro.getTitulo() + "): " + livroDAO.autenticar("Angustia", "Graciliano"));
			} else if (opcao == 2) {
				exibeCodigoTitulo();
				int exibicao = input.nextInt();
				
				if(exibicao == 1) {					
					// Exibir livros por código
					System.out.println("\n\n==== Exibição por Código: === ");
					List<Livro> livros = livroDAO.getOrderByCodigo();
					for (Livro x: livros) {
						System.out.println(x.toString());
					}
				} else if (exibicao == 2) {					
					// Exibir livros por título
					System.out.println("\n\n==== Exibição por Título: === ");
					List<Livro> livros = livroDAO.getOrderByTitulo();
					for (Livro x: livros) {
						System.out.println(x.toString());
					}
				} else {
					System.out.println("Exibição inválida!");
				}
			} else if (opcao == 3) {				
				System.out.println("\n\n==== Atualizar Autor (código (" + livro.getCodigo() + ") === ");
				livro.setAutor("Ramos");
				livroDAO.update(livro);
				
				System.out.println("\n\n==== Testando Autenticação ===");
				System.out.println("Usuário (" + livro.getTitulo() + "): " + livroDAO.autenticar("Angustia", "Ramos"));		
				
				System.out.println("\n\n==== Invadir usando SQL Injection ===");
				System.out.println("Livro (" + livro.getTitulo() + "): " + livroDAO.autenticar("Angustia", "x' OR 'x' LIKE 'x"));
			} else {				
				System.out.println("\n\n==== Excluir Livro (código " + livro.getCodigo() + ") === ");
				livroDAO.delete(livro.getCodigo());
			}				
		
			exibeMenu();
			opcao = input.nextInt();
		}
		
		System.out.println("\n\n==== FIM DO PROGRAMA ====");
		
		input.close();
	}
	
	public static void exibeMenu() {
		System.out.println("\nMENU DE OPÇÕES:");
		System.out.println("1) Inserir");
		System.out.println("2) Listar");
		System.out.println("3) Atualizar");
		System.out.println("4) Excluir");
		System.out.println("5) Sair");
	}
	
	public static void exibeCodigoTitulo() {
		System.out.println("ORDENAR POR CÓDIGO OU POR TÍTULO?");
		System.out.println("1) Código");
		System.out.println("2) Título");
	}
}