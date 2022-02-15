package com.curso.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.curso.daos.PaisDAO;
import com.curso.domain.Pais;

public class PruebaJDBCTransaccion {

	public static void main(String[] args) {
		
		//1. Driver
		try {
			// carga el driver Oracle xe 11g
			Class.forName("oracle.jdbc.OracleDriver");
			System.out.println("cargó driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		//2. Obtener una conexión
		
		// conexión con la bd
		String url= "jdbc:oracle:thin:@localhost:49161:xe";
		String usr = "SYSTEM";
		String pwd = "oracle";
				
		//try recurso que siempre falle o cierra el recurso
		try(Connection con = DriverManager.getConnection(url,usr, pwd);) {
			try {
				//iniciar una transacción 
				con.setAutoCommit(false); // desactivamos el autocommit

				PaisDAO dao = new PaisDAO();
//				Pais p = new Pais("X2","Mi Pais", 4);
//				dao.create(p, con);
//				p.setNombrePais("Mi Pais 2");
//				
//				dao.update(p, con);
				
				Pais p = dao.getByID("X2",con);
				p.setIdRegion(2);
				dao.update(p, con);
				
				
				Pais p2 = dao.getByID("FR",con);
				System.out.println(p2);
				p2.setIdRegion(5); // error oracle integridad FK region 5 no existe
				dao.update(p2, con);
				
				
				System.out.println(p);
				//fin de la transacción
				con.commit();
				System.out.println("TODO OK");
				
			}catch(SQLException e) {
				con.rollback();
				e.printStackTrace();
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}

		
		
	}//fin main

}
