package ar.edu.unahur.obj2.servidorWeb

// Para no tener los códigos "tirados por ahí", usamos un enum que le da el nombre que corresponde a cada código
// La idea de las clases enumeradas es usar directamente sus objetos: CodigoHTTP.OK, CodigoHTTP.NOT_IMPLEMENTED, etc
enum class CodigoHttp(val codigo: Int) {
  OK(200),
  NOT_IMPLEMENTED(501),
  NOT_FOUND(404),
}

class ServidorWeb() {
  val modulos = mutableListOf<Modulo>()

  fun agregarModulo(modulo : Modulo){
      modulos.add(modulo)
  }

  fun quitarModulo(modulo: Modulo){
    modulos.remove(modulo)
  }

  fun verificarProtocolo(pedido : Pedido) : Respuesta {
    return if (pedido.protocolo() != "http")
      Respuesta(CodigoHttp.NOT_IMPLEMENTED, "", 10, pedido)
    else
      Respuesta(CodigoHttp.OK, "hola, esta es una respuesta del servidor", 12, pedido)
  }

  /*
  fun filtrarNulos(pedido: Pedido) : Modulo? {
    return modulos.find{it.puedeTrabajar(pedido.extension()) }
  }
  */


  // NULL - verificar esto
  fun buscarModuloQuePuedaTrabajarCon(pedido : Pedido) : Modulo {
    TODO() //return modulos.any{it.puedeTrabajar(pedido.extension())}
  }


  fun noHayModuloQuePuedaTrabajar(pedido: Pedido) : Boolean{
    return modulos.find{it.puedeTrabajar(pedido.extension())} == null
  }


  fun atenderPedido(pedido: Pedido): Respuesta {
    val respuestaObtenida = this.verificarProtocolo(pedido)

    return if (respuestaObtenida.codigo != CodigoHttp.OK){
      respuestaObtenida
    }else{
      if (modulos.isNotEmpty()){
        if (this.noHayModuloQuePuedaTrabajar(pedido)){
          Respuesta(CodigoHttp.NOT_FOUND, "", 10, pedido)
        }else{
          this.buscarModuloQuePuedaTrabajarCon(pedido).respuestaDelModulo(pedido)
        }
      }else{
        verificarProtocolo(pedido)
      }
    }






  }




}




