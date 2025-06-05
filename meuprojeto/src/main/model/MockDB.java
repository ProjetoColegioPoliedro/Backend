package model;

public class MockDB {
     public int verify(String nome, String senha){
        if(nome.equals("Admin@Admin") && senha.equals("Admin")){
            return 1;
        }
        else{
            return 0;
        }
    }
}
