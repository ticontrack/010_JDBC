package com.curso.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.curso.domain.Pais;

public class PaisDAO {

	private final String insert = "INSERT INTO HR.COUNTRIES VALUES " + "(?, ?, ?)";
	private final String update = "UPDATE HR.COUNTRIES " 
				+ "SET COUNTRY_NAME = ?, " 
				+ "REGION_ID = ? "
				+ "WHERE COUNTRY_ID = ?";
	private final String delete = "DELETE HR.COUNTRIES " + "WHERE COUNTRY_ID = ?";

	private final String selectById = "SELECT COUNTRY_ID, COUNTRY_NAME, REGION_ID " 
								+ "FROM HR.COUNTRIES "
								+ "WHERE COUNTRY_ID = ? ";
	private final String selectAll = "SELECT COUNTRY_ID, COUNTRY_NAME, REGION_ID " 
									+ "FROM HR.COUNTRIES";

	// CRUD
	public void create(Pais p, Connection con) throws SQLException {

		if (p == null) {
			throw new IllegalArgumentException("Falta indicar el pais");
		}

		PreparedStatement insertPst = con.prepareStatement(insert);
		insertPst.setString(1, p.getCodigoPais());
		insertPst.setString(2, p.getNombrePais());
		insertPst.setInt(3, p.getIdRegion());

		insertPst.executeUpdate();
	}

	public void update(Pais p, Connection con) throws SQLException {
		PreparedStatement updatePst = con.prepareStatement(update);
		updatePst.setString(1, p.getNombrePais());
		updatePst.setInt(2, p.getIdRegion());
		updatePst.setString(3, p.getCodigoPais());

		updatePst.executeUpdate();
	}

	public void delete(String codigoPais, Connection con) throws SQLException {
		PreparedStatement deletePst = con.prepareStatement(delete);
		deletePst.setString(1, codigoPais);
		deletePst.executeUpdate();
	}

	public Collection<Pais> getAll(Connection con) throws SQLException {

		List<Pais> paises = new ArrayList<Pais>();

		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(selectAll);
		while (rs.next()) {
			Pais p = new Pais(rs.getString(1), rs.getString(2), rs.getInt(3));
			paises.add(p);
		}
		return paises;

	}

	/**
	 * Devuelve un pais buscanod por su código de pais
	 * Si no lo encuentra devolverá null
	 * @param codigoPais
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public Pais getByID(String codigoPais, Connection con) throws SQLException {
		Pais pais = null;
		PreparedStatement selectPst = con.prepareStatement(selectById);
		selectPst.setString(1, codigoPais);
		ResultSet rs = selectPst.executeQuery();
		if (rs.next()) {
			pais = new Pais(rs.getString(1), rs.getString(2), rs.getInt(3));
		}
		return pais;
	}

}
