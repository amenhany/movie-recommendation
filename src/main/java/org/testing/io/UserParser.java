package org.testing.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import org.testing.model.User;

import java.util.List;
import java.util.Set;

public class UserParser implements Parser<User> {
    Set<String> existingIds = new HashSet<>();

    @Override
    public List<User> parse(List<String> lines) throws IllegalArgumentException {
        List<User> users = new ArrayList<>();

        for(int i=0; i<lines.size();i+=2){
            if (i + 1 >= lines.size()) {
                throw new IllegalArgumentException("ERROR: Missing move id line for last user");
            }
            users.add(validate(lines.get(i), lines.get(i+1)));
        }

        existingIds.clear();
        return users;
    }

    private User validate(String fl, String sl) {
        String[] firstLine = fl.split(",");
        if (firstLine.length != 2) {
            throw new IllegalArgumentException("ERROR: Invalid user name, id line");
        }

        String name = firstLine[0];
        String id = firstLine[1];

        validateUserName(name);
        validateUserId(id);

        List<String> likedMovieIds = Arrays.asList(sl.split(","));
        for (String movieId : likedMovieIds) {
            validateMovieId(movieId);
        }

        return new User(name, id, likedMovieIds);
    }

    private void validateUserName(String name){
        if(name.isEmpty()){
            throw new IllegalArgumentException("ERROR: User Name cannot be empty");
        }
        if(name.charAt(0) == ' '){
            throw new IllegalArgumentException("ERROR: User Name " + name + " is wrong");
        }
        if(!name.matches("^[A-Za-z ]+$")){
            throw new IllegalArgumentException("ERROR: User Name " + name + " is wrong");
        }
    }

    private void validateUserId(String id){
        if (!id.matches("\\d{9}|\\d{8}[a-zA-Z]")) {
            throw new IllegalArgumentException("ERROR: User Id " + id + " is wrong");
        }
        if(existingIds.contains(id)){
            throw new IllegalArgumentException("ERROR: User Id " + id + " is not unique");
        }
        else{
            existingIds.add(id);
        }
    }

    private void validateMovieId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ERROR: Movie Id cannot be null or blank");
        }

        if (!id.toUpperCase().equals(id)) {
            throw new IllegalArgumentException("ERROR: Movie Id letters " + id + " are wrong");
        }

        //check digits part
        int i = 0;
        while (i < id.length() && !Character.isDigit(id.charAt(i))) {
            i++;
        }
        String digits = id.substring(i);

        if (!digits.matches("\\d{3}")) {
            throw new IllegalArgumentException("ERROR: Movie Id digits " + id + " must be exactly 3 digits");
        }
        if (digits.charAt(0) == digits.charAt(1) || digits.charAt(1) == digits.charAt(2) || digits.charAt(0) == digits.charAt(2)) {
            throw new IllegalArgumentException("ERROR: Movie Id numbers " + id + " aren't unique");
        }
    }
}
