package ar.edu.unahur.obj2.servidorWeb

class Modulo(val extensiones:List<String>, val texto : String, val tiempo : Int ) {
     var cantConsultasSospechosas : Int = 0

     val respuestasDemoradas = mutableListOf<Respuesta>()

     fun puedeTrabajar(extension: String) = extensiones.contains(extension)

     fun respuestaDelModulo(pedido:Pedido): Respuesta = Respuesta(CodigoHttp.OK,texto,tiempo,pedido)

     fun sumarConsultaSospechosa()  {
          cantConsultasSospechosas += 1
     }

}

