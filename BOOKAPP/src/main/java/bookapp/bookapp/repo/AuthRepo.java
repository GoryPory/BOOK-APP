/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookapp.bookapp.repo;

import bookapp.bookapp.entity.Users;
import bookapp.bookapp.model.UsersModel;

/**
 *
 * @author gori
 */
public class AuthRepo {
    private static AuthRepo instance;
    private final UsersModel usersModel = new UsersModel();
    private Users currentUser;

    private AuthRepo() {}

    public static AuthRepo getInstance() {
        if (instance == null) {
            instance = new AuthRepo();
        }
        return instance;
    }

    public boolean login(String username, String password) {
        Users user = usersModel.login(username, password);
        if(user == null){
            return false;
        }
        currentUser = user;
        return true;
    }

    public void logout (){
        currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public Users getCurrentUser() {
        return currentUser;
    }

    public boolean isAdmin() {
        return currentUser != null && "Admin".equalsIgnoreCase(currentUser.getRole());
    }

    public boolean isKasir() {
        return currentUser != null && "Kasir".equalsIgnoreCase(currentUser.getRole());
    }

}

