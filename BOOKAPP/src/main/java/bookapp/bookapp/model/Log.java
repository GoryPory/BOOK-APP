/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookapp.bookapp.model;

/**
 *
 * @author radit
 */
import java.util.List;

public class Log implements Base<Log, Integer> {

    @Override
    public List<Log> readAll() {
        // TODO: Implementasi SELECT * FROM log_inventori
        return null; 
    }

    @Override
    public List<Log> search(String keyword) {
        // TODO: (SURE/Opsional) Implementasi pencarian log
        return null;
    }
}