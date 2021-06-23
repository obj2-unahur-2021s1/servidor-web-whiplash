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
  val analizadores = mutableListOf<Analizador>()

  fun agregarModulo(modulo : Modulo){
    modulos.add(modulo)
  }

  fun quitarModulo(modulo: Modulo){
    modulos.remove(modulo)
  }

  fun agregarAnalizador(analizador : Analizador){
    analizadores.add(analizador)
  }

  fun quitarAnalizador(analizador:Analizador){
    analizadores.remove(analizador)
  }

  fun listaDeModulosNoEstaVacia() = modulos.isNotEmpty()

  fun listaDeAnalizadoresNoEstaVacia() = analizadores.isNotEmpty()

  fun verificarProtocolo(pedido : Pedido) : Respuesta {
    return if (pedido.protocolo() != "http")
      Respuesta(CodigoHttp.NOT_IMPLEMENTED, "", 10, pedido)
    else
      Respuesta(CodigoHttp.OK, "hola, esta es una respuesta del servidor", 12, pedido)
  }

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

  fun mandarAAnalizar(respuestaFinal: Respuesta, modulo : Modulo) =
    analizadores.forEach{it.analizar(respuestaFinal,modulo)}


  fun atenderPedido(pedido: Pedido) :Respuesta{
    lateinit var respuestaObtenida : Respuesta
    lateinit var moduloSeleccionado : Modulo

    respuestaObtenida = this.verificarProtocolo(pedido)

    if (this.listaDeModulosNoEstaVacia()){
      if(this.existeModuloQuePuedaTrabajarCon(pedido)) {
        moduloSeleccionado = this.buscarModuloQuePuedaTrabajarCon(pedido)
      }
      respuestaObtenida = this.respuestaDelModulo(pedido)
    }

    if(this.listaDeAnalizadoresNoEstaVacia() && this.listaDeModulosNoEstaVacia()){
      this.mandarAAnalizar(respuestaObtenida, moduloSeleccionado)
    }

    return respuestaObtenida

  }


}




