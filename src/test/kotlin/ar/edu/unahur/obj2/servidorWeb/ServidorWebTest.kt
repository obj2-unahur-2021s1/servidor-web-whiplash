package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class ServidorWebTest : DescribeSpec({
  describe("Un servidor web") {
    val servidorWeb = ServidorWeb()
    val unPedido = Pedido("192.168.1.102", "http://www.pipo.com/imagen.jpg",LocalDateTime.now())
    val otroPedido = Pedido("192.168.1.10", "ftp://juampi.com/virus.exe", LocalDateTime.now())
    val moduloDeImagen = Modulo(listOf("png","jpg","gif","raw"), "soy un modulo de imagen", 10)

    //servidor sin modulos
    it("Devuelve 200/OK protocolo correcto"){
      val respuestaObtenida = servidorWeb.atenderPedido(unPedido)
      respuestaObtenida.codigo.shouldBe(CodigoHttp.OK)
      respuestaObtenida.body.shouldBe("hola, esta es una respuesta del servidor")
      respuestaObtenida.tiempo.shouldBe(12)
    }

    //servidor sin modulos
    it("Devuelve 501/no implementado"){
      val respuestaObtenida = servidorWeb.atenderPedido(otroPedido)
      respuestaObtenida.codigo.shouldBe(CodigoHttp.NOT_IMPLEMENTED)
      respuestaObtenida.body.shouldBe("")
      respuestaObtenida.tiempo.shouldBe(10)
    }

  }

  describe("un servidor web con modulos"){
    val servidorWeb = ServidorWeb()
    val unPedidoConImagen = Pedido("192.168.1.102", "http://www.pipo.com/imagen.jpg",LocalDateTime.now())
    val otroPedidoConTexto = Pedido("192.168.1.10", "http://juampi.com/virus.doc", LocalDateTime.now())
    val moduloDeImagen = Modulo(listOf("png","jpg","gif","raw"), "soy un modulo de imagen", 10)
    servidorWeb.agregarModulo(moduloDeImagen)

    it("no hay un modulo que puede trabajar con el pedido"){
      val respuestaObtenida = servidorWeb.atenderPedido(otroPedidoConTexto)

      respuestaObtenida.codigo.shouldBe(CodigoHttp.NOT_FOUND)
      respuestaObtenida.body.shouldBe("")
      respuestaObtenida.tiempo.shouldBe(10)
      respuestaObtenida.pedido.shouldBe(otroPedidoConTexto)
    }


  }

})
