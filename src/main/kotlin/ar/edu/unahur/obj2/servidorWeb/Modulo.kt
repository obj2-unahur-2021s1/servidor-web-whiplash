package ar.edu.unahur.obj2.servidorWeb

class Modulo(val extensiones:List<String>, val texto : String, val tiempo : Int ) {

     fun puedeTrabajar(extension: String) = extensiones.contains(extension)

     fun respuestaDelModulo(pedido:Pedido): Respuesta = Respuesta(CodigoHttp.OK,texto,tiempo,pedido)


}

