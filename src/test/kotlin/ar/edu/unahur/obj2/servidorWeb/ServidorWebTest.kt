package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class ServidorWebTest : DescribeSpec({
  describe("Un servidor web") {
    val servidorWeb = ServidorWeb()
    val unPedido = Pedido("192.168.1.102", "http://www.pipo.com/imagen.jpg",LocalDateTime.now())
    val otroPedido = Pedido("192.168.1.10", "ftp://juampi.com/virus.exe", LocalDateTime.now())


    it("Devuelve ok si el protocolo es HTTP"){
      val respuestaObtenida = servidorWeb.atenderPedido(unPedido)
      respuestaObtenida.codigo.shouldBe(CodigoHttp.OK)
      respuestaObtenida.body.shouldBe("hola")
      respuestaObtenida.tiempo.shouldBe(12)
    }

    it("Devuelve 501 no implementado"){
      val respuestaObtenida = servidorWeb.atenderPedido(otroPedido)
      respuestaObtenida.codigo.shouldBe(CodigoHttp.NOT_IMPLEMENTED)
      respuestaObtenida.body.shouldBe("")
      respuestaObtenida.tiempo.shouldBe(10)
    }

  }
})
