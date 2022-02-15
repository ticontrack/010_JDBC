package com.curso.jdbc;

import java.sql.*;

public class Application
{
	public static void main( String[] args ){
		
		//1. Driver
		try {
			// carga el driver Oracle xe 11g
			Class.forName("oracle.jdbc.OracleDriver");
			System.out.println("cargó driver");
				
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		//2. Conexion
		
		// conexión con la bd
		String url= "jdbc:oracle:thin:@localhost:49161:xe";
		String usr = "SYSTEM";
		String pwd = "oracle";
		
		//try recurso que siempre falle o cierra el recurso
		try(Connection con = DriverManager.getConnection(url,usr, pwd);) {

			//conectado
			System.out.println("Conectado con la bd");
			
			// consultas de tipo SELECT
			String consulta = "SELECT REGION_ID, COUNTRY_NAME, COUNTRY_ID "
					+ "FROM HR.COUNTRIES";
			
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(consulta); //ejecuta una select
			while(rs.next()) {
				//leer campos
				//String countryId = rs.getString(1); //1 numero de columan
				int regionId = rs.getInt("REGION_ID");
				String  name = rs.getString(2);
				String id = rs.getString(3);
				
				System.out.printf(" %s %s %d %n", id, name, regionId);
			}
		
			System.out.println(".......");
			 //busco por id contry
			//String codigo = "BE' or '1'='1";
			String codigo = "BE";
			consulta = "SELECT REGION_ID, COUNTRY_NAME, COUNTRY_ID "
					+ "FROM HR.COUNTRIES "
					+ "WHERE COUNTRY_ID = '" + codigo +"'";
			
			rs = st.executeQuery(consulta);
			
			if(rs.next()) {
				System.out.println(" pais es " + rs.getString(2));
			}else {
				System.out.println("Pais no existe");
			}
			
			// modo seguro 
			System.out.println("..... PreparedStatement");
			PreparedStatement pst = con.prepareStatement("SELECT * "
					+ "FROM HR.COUNTRIES "
					+ "WHERE COUNTRY_ID = ?");
			
			codigo = "ZZ" ;
			pst.setString(1, codigo);  /// "BE" -> 'BE'
			                           //"BE' or '1'='1"  -> "BE' or '1'='1"
			
		    rs =  pst.executeQuery();
			
			if(rs.next()) {
				System.out.println(" pais es " + rs.getString(2));
			}else {
				System.out.println("Pais no existe");
			}
			
//			String insercion = "INSERT INTO HR.COUNTRIES VALUES "
//					+ "(?, ?, ?)";
//			PreparedStatement insertPst = con.prepareStatement(insercion);
//			insertPst.setString(1,"ZZ");
//			insertPst.setString(2, "Zamunda");
//			insertPst.setInt(3, 4); // 4 id region
//			
//			int contadorRegAfectados = insertPst.executeUpdate();
//			
//			System.out.printf("Ha insertado %d registro %n",contadorRegAfectados );
//			
			
			//Lanzar una instrucción para modificar el nombre el  pasis
			//cuyo id es "ZZ"
			//PreparedStatement
			PreparedStatement update = con.prepareStatement("UPDATE HR.COUNTRIES "
					+ "SET COUNTRY_NAME = ? "
					+ "WHERE COUNTRY_ID = ?");
			update.setString(1, "Zamunda Modif");
			update.setString(2, "ZZ");
			
			int cambiados = update.executeUpdate();
			
			
			PreparedStatement delete = con.prepareStatement(
					"DELETE HR.COUNTRIES "
					+ "WHERE COUNTRY_ID = ?");
			delete.setString(1, "ZZ");
			
			//int borrados = delete.executeUpdate();
			
			//System.out.printf("Borró %d paises. %n", borrados);
			
			//LLAMAR A UN PROC ALMACENADO
			
			CallableStatement cst = con.prepareCall("{call HR.subir_sueldos(?,?,?)}");
			
			//1 ?  id deparamento
			//2 ?  subida
			//3 out  total reg modif
			int dpto = 90;
			int salario = 20;
			cst.setInt(1, dpto);
			cst.setInt(2, salario);
			
			cst.registerOutParameter(3,  Types.INTEGER);
			
			cst.execute();
			
			int total = cst.getInt(3);
			
			System.out.printf("Has cambiado el sarario de %d empleados. %n" , total);

			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
}