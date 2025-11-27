package org.testing.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import org.testing.model.User;

import java.util.List;
import java.util.Set;

public class UserParser implements Parser<User> {
    @Override
    public List<User> parse(List<String> lines) throws IllegalArgumentException {
        List<User> users = new ArrayList<>();
        Set<String> existingIds = new HashSet<>();
        
        for(int i=0; i<lines.size();i+=2){
            String validatedLine;
            
            String name = lines.get(i).split(",")[0];
            String id = lines.get(i).split(",")[1];
            validateUserName(name);
            validateUserId(id);
            //Check ID is unique
            if(existingIds.contains(id)){
                throw new IllegalArgumentException("User ID " + id + "is not unique.");
            }
            else{
                existingIds.add(id);
            }
            List<String> likedMovieIds = Arrays.asList(lines.get(i+1).split(","));
            users.add(new User(name, id, likedMovieIds));
        }
        return users;
    }
    
    private void validateUserName(String name){
        //Checking requirements for user name
        if(!name.matches("[a-zA-Z]+")){
            throw new IllegalArgumentException("User's name must be alphabetic");
        }
        if(name.charAt(0) == ' '){
            throw new IllegalArgumentException("User's name must NOT start with space");
        }
        if(name.isEmpty() || name == null){
            throw new IllegalArgumentException("User's name cannot be empty");
        }
    }

    private void validateUserId(String id){
        if (!id.matches("\\d{8}[a-zA-Z]?")) {
        throw new IllegalArgumentException(
            "User ID must be 9 characters: start with numbers and optionally end with one letter");
        }
        
    }
}
