package Modelo

import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {

    fun cadenaConexion(): Connection? {

        try {
            val url = "jdbc:oracle:thin:@10.10.1.119:1521:xe"
            val usuario = "system"
            val contrana = "desarrollo"

            val conection = DriverManager.getConnection(url, usuario, contrana)

            return conection
        }
        catch (e: Exception){
            println("Este es el error: $e")
            return null
        }
    }
}