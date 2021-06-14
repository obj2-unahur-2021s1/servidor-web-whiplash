package ar.edu.unahur.obj2.servidorWeb

abstract class Modulo(extensiones:List<String>, texto : String, tiempo : Int ) {

     fun puedeTrabajar(extension:String){

     }


}

class ModuloImagenes(extensiones: List<String>, texto: String, tiempo: Int) : Modulo(extensiones, texto, tiempo) {



}
