package org.example.trainstation.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database{

    private static final String user = "root";
    private static final String mypassword = "qwerty1310ytrewq";
    private static final String url = "jdbc:mysql://localhost:3306/users";
    private static boolean isLogin = false;
    private static String loginuse;

    public static void register(String login, String password){
        try {
            if (!checkUser(login)) {

                Connection connection = DriverManager.getConnection(url, user, mypassword);

                PreparedStatement insertUser = connection.prepareStatement("INSERT INTO " + "`users`.`users`" + "(login, password)" + "VALUES (?, ?)");

                insertUser.setString(1, login);
                insertUser.setString(2, password);

                insertUser.executeUpdate();

            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    } // працює

    public static boolean checkLogin(String login, String password) {
        try {
            Connection connection = DriverManager.getConnection(url, user, mypassword);
            String query = "SELECT * FROM users WHERE login=? AND password=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean isValidLogin = resultSet.next();
            resultSet.close();
            preparedStatement.close();
            connection.close();
            return isValidLogin;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    } // працює
    public static boolean checkUser(String login){
        try{
            Connection connection = DriverManager.getConnection(url, user, mypassword);

            PreparedStatement checkUserExists = connection.prepareStatement("select * from " + "`users`.`users`" + " where login = ? ");

            checkUserExists.setString(1, login);

            ResultSet resultSet = checkUserExists.executeQuery();

            if(!resultSet.isBeforeFirst()){
                return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return true;
    } // працює
    public static boolean isAdmin(String login) {
        try {
            Connection connection = DriverManager.getConnection(url, user, mypassword);
            PreparedStatement checkAdmin = connection.prepareStatement("SELECT * FROM `users`.`users` WHERE login = ? AND admin = 1");
            checkAdmin.setString(1, login);
            ResultSet resultSet = checkAdmin.executeQuery();
            boolean isAdmin = resultSet.next();
            return isAdmin;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    } // працює
    public static void addTicket(String from, String to, String date, String time,String price){
        try {
                Connection connection = DriverManager.getConnection(url, user, mypassword);
            PreparedStatement insertUser = connection.prepareStatement("INSERT INTO `users`.`trains` (`From`, `To`, `date`,`time`,`price`) VALUES (?, ?, ?, ?, ?)");
            insertUser.setString(1, from);
            insertUser.setString(2, to);
            insertUser.setString(3, date);
            insertUser.setString(4, time);
            insertUser.setString(5, price);
            insertUser.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }

    } // працює
    public static boolean checkTrain(String From, String To, String date) {
        String query = "SELECT * FROM trains WHERE `From`=? AND `To`=? AND `date`=?";
        try (Connection connection = DriverManager.getConnection(url, user, mypassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, From);
            preparedStatement.setString(2, To);
            preparedStatement.setString(3, date);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    } // працює
    public static List<String> getAllTrains(String from, String to, String date1) {
            List<String> trains = new ArrayList<>();
            String sql = "SELECT * FROM trains WHERE `From` = ? AND `To` = ? AND date = ?";

            try (Connection conn = DriverManager.getConnection(url, user, mypassword);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, from);
                stmt.setString(2, to);
                stmt.setString(3, date1);

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int trainId = rs.getInt("idTrains");
                    String From = rs.getString("From");
                    String To = rs.getString("To");
                    String date = rs.getString("date");
                    String time = rs.getString("time");
                    String price = rs.getString("price");
                    String train = ("Номер потягу: "+trainId+";"+" Звідки: "+From+";"+" Куди: "+To+";"+" Дата: "+date+";"+" Час: "+time+";"+" Ціна: "+price+";");
                    trains.add(train);
                    System.out.println(trains);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return trains;
    } // працює
    public static boolean buyTrain(String from, String to, String date, String time, String trainid) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, mypassword);
            PreparedStatement insertUser = connection.prepareStatement(
                    "INSERT INTO `users`.`tickets` (id_trains, login, `from`, `to`, date, time) VALUES (?, ?, ?, ?, ?, ?)");
            insertUser.setString(1, trainid);
            insertUser.setString(2, loginuse);
            insertUser.setString(3, from);
            insertUser.setString(4, to);
            insertUser.setString(5, date);
            insertUser.setString(6, time);
            insertUser.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    } // працює

    public static List<String> getAllUserTrains() {
        List<String> trainsuser = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE login = ?";
        try (Connection conn = DriverManager.getConnection(url, user, mypassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, loginuse);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int trainId = rs.getInt("id_trains");
                String from = rs.getString("from");
                String to = rs.getString("to");
                String date = rs.getString("date");
                String time = rs.getString("time");
                String train = ("Номер потягу: "+trainId+";"+" Звідки: "+from+";"+" Куди: "+to+";"+" Дата: "+date+";"+" Час: "+time);
                trainsuser.add(train);
                System.out.println(trainsuser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trainsuser;
    } // працює
    public static void delete(){
        try {
            Connection connection = DriverManager.getConnection(url, user, mypassword);

            String query = "DELETE FROM trains WHERE `date` < ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setDate(1, new java.sql.Date(System.currentTimeMillis()));

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected); // Друк кількості видалених записів (додатково)
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } // працює
    public static void delete(String login){
        try {
            Connection connection = DriverManager.getConnection(url, user, mypassword);

            String query = "DELETE FROM tickets WHERE `date` < ? and `login`=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setDate(1, new java.sql.Date(System.currentTimeMillis()));
            preparedStatement.setString(2, login);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } // працює
    public static boolean isIsLogin() {
        return isLogin;
    }

    public static void setIsLogin(boolean isLogin) {
        Database.isLogin = isLogin;
    }
    public static String isloginuse() {
        return loginuse;
    }

    public static void setLoginuse(String loginuse) {
        Database.loginuse = loginuse;
    }
}