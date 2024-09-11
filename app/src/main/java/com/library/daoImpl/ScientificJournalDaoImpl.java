package com.library.daoImpl;

import com.library.dao.ScientificJournalDAO;
import com.library.model.ScientificJournal;
import com.library.utils.db.DbConnection;

import java.sql.*;
import java.util.HashMap;

public class ScientificJournalDaoImpl implements ScientificJournalDAO {

    private Connection cn;

    public ScientificJournalDaoImpl() {
        this.cn = DbConnection.connect();
    }

    @Override
    public HashMap<String, ScientificJournal> getAll() {
        HashMap<String, ScientificJournal> journals = new HashMap<>();
        String query = "SELECT * FROM scientific_journals";
        try (PreparedStatement preparedStatement = cn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                ScientificJournal journal = new ScientificJournal(
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getDate("pub_date").toLocalDate(),
                        resultSet.getInt("num_pages"),
                        resultSet.getInt("impact_factor")
                );
                journals.put(resultSet.getString("id"), journal);
            }
        } catch (SQLException e) {
            System.out.println("[-] SQL error: " + e);
        }
        return journals;
    }

    @Override
    public ScientificJournal get(String id) {
        String query = "SELECT * FROM scientific_journals WHERE id = ?";
        try (PreparedStatement preparedStatement = cn.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new ScientificJournal(
                            resultSet.getString("title"),
                            resultSet.getString("author"),
                            resultSet.getDate("pub_date").toLocalDate(),
                            resultSet.getInt("num_pages"),
                            resultSet.getInt("impact_factor")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("[-] SQL error: " + e);
        }
        return null;
    }

    @Override
    public void create(ScientificJournal scjou) {
        String query = "INSERT INTO scientific_journals (title, author, pub_date, num_pages, impact_factor) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement prestm = cn.prepareStatement(query)) {
            prestm.setString(1, scjou.getTitle());
            prestm.setString(2, scjou.getAuthor());
            prestm.setDate(3, Date.valueOf(scjou.getPubDate()));
            prestm.setInt(4, scjou.getNumPages());
            prestm.setDouble(5, scjou.getImpactFactor());
            prestm.executeUpdate();
            System.out.println("[+] Scientific Journal added.");
        } catch (SQLException e) {
            System.out.println("[-] SQL error: " + e);
        }
    }

    @Override
    public void delete(ScientificJournal scJou) {
        String query = "DELETE FROM scientific_journals WHERE id = ?";
        try (PreparedStatement prestm = cn.prepareStatement(query)) {
            prestm.setString(1, scJou.getId());
            prestm.executeUpdate();
            System.out.println("[+] Scientific Journal deleted.");
        } catch (SQLException e) {
            System.out.println("[-] SQL error: " + e);
        }
    }

    @Override
    public void update(ScientificJournal scJou) {
        String query = "UPDATE scientific_journals SET title = ?, author = ?, pub_date = ?, num_pages = ? WHERE id = ?";
        try (PreparedStatement prestm = cn.prepareStatement(query)) {
            prestm.setString(1, scJou.getTitle());
            prestm.setString(2, scJou.getAuthor());
            prestm.setDate(3, Date.valueOf(scJou.getPubDate()));
            prestm.setInt(4, scJou.getNumPages());
            prestm.setDouble(5, scJou.getImpactFactor());
            prestm.setString(6, scJou.getId());
            prestm.executeUpdate();
            System.out.println("[+] Scientific Journal updated.");
        } catch (SQLException e) {
            System.out.println("[-] SQL error: " + e);
        }
    }
}