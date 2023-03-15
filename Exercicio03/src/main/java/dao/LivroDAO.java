package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Livro;

public class LivroDAO extends DAO {
	
	public LivroDAO() {
		super();
		conectar();
	}

	public void finalize() {
		close();
	}
	
	
	public boolean insert(Livro livro) {
		boolean status = false;
		
		// Condição para impossibilitar valores inválidos
		if(livro.getAno() > 0 && livro.getTitulo() != "" && livro.getAutor() != "") {
			try {  
				Statement st = conexao.createStatement();
				String sql = "INSERT INTO livro (titulo, autor, ano) "
					       + "VALUES ('"+ livro.getTitulo() + "', '"  
					       + livro.getAutor() + "', " + livro.getAno() + ");";
				System.out.println(sql);
				st.executeUpdate(sql);
				st.close();
				status = true;
			} catch (SQLException u) {  
				throw new RuntimeException(u);
			}
		}
		
		return status;
	}

	
	public Livro get(int codigo) {
		Livro livro = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM livro WHERE codigo=" + codigo;
			System.out.println(sql);
			ResultSet rs = st.executeQuery(sql);	
	        if(rs.next()){            
	        	 livro = new Livro(rs.getInt("codigo"), rs.getString("titulo"), rs.getString("autor"), rs.getInt("ano"));
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return livro;
	}
	
	
	public List<Livro> get() {
		return get("");
	}

	
	public List<Livro> getOrderByCodigo() {
		return get("codigo");		
	}
	
	
	public List<Livro> getOrderByTitulo() {
		return get("titulo");		
	}
	
	public List<Livro> getOrderByAutor() {
		return get("autor");		
	}
	
	
	public List<Livro> getOrderByAno() {
		return get("ano");		
	}
	
	
	private List<Livro> get(String orderBy) {	
	
		List<Livro> livros = new ArrayList<Livro>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM livro" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			System.out.println(sql);
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	Livro u = new Livro(rs.getInt("codigo"), rs.getString("titulo"), rs.getString("autor"), rs.getInt("ano"));
	            livros.add(u);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return livros;
	}	
	
	public boolean update(Livro livro) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			String sql = "UPDATE livro SET titulo = '" + livro.getTitulo() + "', autor = '"  
				       + livro.getAutor() + "', ano = '" + livro.getAno() + "'"
					   + " WHERE codigo = " + livro.getCodigo();
			System.out.println(sql);
			st.executeUpdate(sql);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	public boolean delete(int codigo) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			String sql = "DELETE FROM livro WHERE codigo = " + codigo;
			System.out.println(sql);
			st.executeUpdate(sql);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	
	public boolean autenticar(String titulo, String autor) {
		boolean resp = false;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM livro WHERE titulo LIKE '" + titulo + "' AND autor LIKE '" + autor  + "'";
			System.out.println(sql);
			ResultSet rs = st.executeQuery(sql);
			resp = rs.next();
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return resp;
	}	
}