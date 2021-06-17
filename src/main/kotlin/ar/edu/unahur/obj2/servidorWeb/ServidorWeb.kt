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

  fun listaDeModulosNoEstaVacia() = modulos.isNotEmpty()

  fun verificarProtocolo(pedido : Pedido) : Respuesta {
    return if (pedido.protocolo() != "http")
      Respuesta(CodigoHttp.NOT_IMPLEMENTED, "", 10, pedido)
    else
      Respuesta(CodigoHttp.OK, "hola, esta es una respuesta del servidor", 12, pedido)
  }

  // NULL - verificar esto
  fun buscarModuloQuePuedaTrabajarCon(pedido : Pedido) : Modulo{
    return modulos.find{it.puedeTrabajar(pedido.extension())}!!
  }

  fun existeModuloQuePuedaTrabajarCon(pedido: Pedido) : Boolean{
    return modulos.any{it.puedeTrabajar(pedido.extension())}
  }

  fun respuestaDelModulo(pedido : Pedido) : Respuesta{
    return if(!this.existeModuloQuePuedaTrabajarCon(pedido)){
      Respuesta(CodigoHttp.NOT_FOUND,"",10,pedido)
    }else{
      this.buscarModuloQuePuedaTrabajarCon(pedido).respuestaDelModulo(pedido)
    }
  }


  fun atenderPedido(pedido: Pedido) : Respuesta{
    val respuestaObtenida = this.verificarProtocolo(pedido)

    return if (respuestaObtenida.codigo != CodigoHttp.OK){
      respuestaObtenida
    }else{
      if (this.listaDeModulosNoEstaVacia()){
        this.respuestaDelModulo(pedido)
      }else{
        verificarProtocolo(pedido)
      }
    }

  }




}




