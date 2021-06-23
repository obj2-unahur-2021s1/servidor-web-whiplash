package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime
import java.time.Month

class ServidorWebTest : DescribeSpec({
  describe("Un servidor web") {
    val servidorWeb = ServidorWeb()
    val unPedido = Pedido("192.168.1.102", "http://www.pipo.com.ar/imagen.jpg",LocalDateTime.now())
    val otroPedido = Pedido("192.168.1.10", "ftp://juampi.com.ar/virus.exe", LocalDateTime.now())


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
    val unPedidoConImagen = Pedido("192.168.1.102", "http://www.pipo.com.ar/imagen.jpg",LocalDateTime.now())
    val otroPedidoConTexto = Pedido("192.168.1.10", "http://juampi.com.ar/virus.doc", LocalDateTime.now())
    val moduloDeImagen = Modulo(listOf("png","jpg","gif","raw"), "soy un modulo de imagen", 10)
    val moduloDeBinarios = Modulo(listOf("exe","bin"), "soy un modulo de binarios", 10)
    val moduloDeVideo = Modulo(listOf("avi","3gp","mkv","mpeg"), "soy un modulo de video", 10)

    servidorWeb.agregarModulo(moduloDeBinarios)
    servidorWeb.agregarModulo(moduloDeVideo)
    servidorWeb.agregarModulo(moduloDeImagen)

    it("no hay un modulo que puede trabajar con el pedido"){
      val respuestaObtenida = servidorWeb.atenderPedido(otroPedidoConTexto)

      respuestaObtenida.codigo.shouldBe(CodigoHttp.NOT_FOUND)
      respuestaObtenida.body.shouldBe("")
      respuestaObtenida.tiempo.shouldBe(10)
      respuestaObtenida.pedido.shouldBe(otroPedidoConTexto)
    }

    it("hay Un modulo de imagen que puede trabajar con el pedido "){
      val respuestaObtenida = servidorWeb.atenderPedido(unPedidoConImagen)

      respuestaObtenida.codigo.shouldBe(CodigoHttp.OK)
      respuestaObtenida.body.shouldBe("soy un modulo de imagen")
      respuestaObtenida.tiempo.shouldBe(10)
      respuestaObtenida.pedido.shouldBe(unPedidoConImagen)
    }

  }

  describe("Analizadores"){
    val servidorWeb = ServidorWeb()
    val unPedidoConImagen = Pedido("192.168.1.102", "http://www.pipo.com.ar/imagen.jpg",LocalDateTime.now())
    val otroPedidoConTexto = Pedido("192.168.1.10", "http://juampi.com.ar/virus.doc", LocalDateTime.now())
    val unPedidoDeVideo = Pedido("192.168.1.103", "http://bardo.com.ar/keloke.avi", LocalDateTime.now())
    val moduloDeImagen = Modulo(listOf("png","jpg","gif","raw"), "soy un modulo de imagen", 50)
    val moduloDeTexto = Modulo(listOf("txt","doc", "odt"), "soy un modulo de texto", 9)
    val moduloDeVideo = Modulo(listOf("avi","3gp","mkv","mpeg"), "soy un modulo de video", 20)
    val analizadorDeDemora = DeteccionDeDemoraEnRespuesta(10)

    servidorWeb.agregarModulo(moduloDeTexto)
    servidorWeb.agregarModulo(moduloDeVideo)
    servidorWeb.agregarModulo(moduloDeImagen)
    servidorWeb.agregarAnalizador(analizadorDeDemora)

    servidorWeb.atenderPedido(unPedidoConImagen)
    servidorWeb.atenderPedido(otroPedidoConTexto)
    servidorWeb.atenderPedido(unPedidoDeVideo)


    it("probando analizador deteccion de demora en respuesta"){

      analizadorDeDemora.cantidadDeRespuestasDemoradas(moduloDeImagen).shouldBe(1)

    }

    it ("probando analizador deteccion de demora en respuesta , con modulo no demorado"){
      analizadorDeDemora.cantidadDeRespuestasDemoradas(moduloDeTexto).shouldBe(0)
    }

  }

  describe ("analizadores - Ips sospechosas"){
    val servidorWeb = ServidorWeb()

    val unPedidoConImagen = Pedido("192.168.1.105", "http://www.pipo.com.ar/documentos/imagen.jpg",LocalDateTime.now())
    val otroPedidoConImagen = Pedido("192.168.1.102", "http://www.jorgito.com.ar/documentos/imagen.jpg",LocalDateTime.now())
    val tercerPedidoConImagen = Pedido("192.168.1.102", "http://www.juancito.com.ar/documentos/imagen.jpg",LocalDateTime.now())

    val otroPedidoConTexto = Pedido("192.168.1.150", "http://juampi.com.ar/virus.doc", LocalDateTime.now())


    val unPedidoDeVideo = Pedido("192.168.1.104", "http://bardo.com.ar/keloke.avi", LocalDateTime.now())
    val otroPedidoDeVideo = Pedido("192.168.1.104", "http://iutub.com.ar/avion.avi", LocalDateTime.now())
    val tercerPedidoDeVideo = Pedido("192.168.1.104", "http://github.com.ar/tutorial.avi", LocalDateTime.now())
    val cuartoPedidoDeVideo = Pedido("192.168.1.104", "http://qwerty.com.ar/tutorial.avi", LocalDateTime.now())

    val moduloDeImagen = Modulo(listOf("png","jpg","gif","raw"), "soy un modulo de imagen", 50)
    val moduloDeTexto = Modulo(listOf("txt","doc", "odt"), "soy un modulo de texto", 9)
    val moduloDeVideo = Modulo(listOf("avi","3gp","mkv","mpeg"), "soy un modulo de video", 20)
    val analizadorDeIpsSospechosas = IpsSospechosas(listOf("192.168.1.102","192.168.1.104","192.168.1.105" ))

    servidorWeb.agregarModulo(moduloDeTexto)
    servidorWeb.agregarModulo(moduloDeVideo)
    servidorWeb.agregarModulo(moduloDeImagen)
    servidorWeb.agregarAnalizador(analizadorDeIpsSospechosas)

    servidorWeb.atenderPedido(unPedidoConImagen)
    servidorWeb.atenderPedido(otroPedidoConImagen)
    servidorWeb.atenderPedido(tercerPedidoConImagen)
    servidorWeb.atenderPedido(otroPedidoConTexto)
    servidorWeb.atenderPedido(unPedidoDeVideo)
    servidorWeb.atenderPedido(otroPedidoDeVideo)
    servidorWeb.atenderPedido(tercerPedidoDeVideo)
    servidorWeb.atenderPedido(cuartoPedidoDeVideo)

    it("cuantos pedidos realizo una ip sospechosa"){
      analizadorDeIpsSospechosas.cuantosPedidosRealizo("192.168.1.104").shouldBe(4)
    }

    it("modulo más consultado por ips sospechosas"){
      analizadorDeIpsSospechosas.moduloMasConsultadoPorIpsSospechosas().shouldBe(moduloDeVideo)
    }

    it("conjunto de ips sospechosas que requirieron una cierta ruta"){
      analizadorDeIpsSospechosas.conjuntoDeIpsSospechosas("/documentos/imagen.jpg").shouldContainExactlyInAnyOrder(listOf("192.168.1.102","192.168.1.105" ))
    }

  }

  describe ("estadisticas"){
    val servidorWeb = ServidorWeb()

    val unPedidoConImagen = Pedido("192.168.1.105", "http://www.pipo.com.ar/documentos/imagen.jpg",LocalDateTime.of(2021,Month.JUNE,5,14,14))
    val otroPedidoConImagen = Pedido("192.168.1.102", "http://www.jorgito.com.ar/documentos/imagen.jpg",LocalDateTime.of(2021,Month.JUNE,8,14,14))
    val tercerPedidoConImagen = Pedido("192.168.1.102", "http://www.juancito.com.ar/documentos/imagen.jpg",LocalDateTime.of(2021,Month.JUNE,15,14,14))

    val otroPedidoConTexto = Pedido("192.168.1.150", "http://juampi.com.ar/virus.doc", LocalDateTime.of(2021, Month.JUNE,21,20,12))


    val unPedidoDeVideo = Pedido("192.168.1.104", "http://bardo.com.ar/keloke.avi", LocalDateTime.of(2021, Month.JUNE,2,11,12))
    val otroPedidoDeVideo = Pedido("192.168.1.104", "http://iutub.com.ar/avion.avi", LocalDateTime.of(2021, Month.JUNE,19,12,12))
    val tercerPedidoDeVideo = Pedido("192.168.1.104", "http://github.com.ar/tutorial.avi", LocalDateTime.of(2021, Month.JUNE,17,23,12))
    val cuartoPedidoDeVideo = Pedido("192.168.1.104", "http://qwerty.com.ar/tutorial.avi", LocalDateTime.of(2021, Month.JUNE,20,2,1))
    val moduloDeImagen = Modulo(listOf("png","jpg","gif","raw"), "soy un modulo de imagen, hola! ", 50)
    val moduloDeTexto = Modulo(listOf("txt","doc", "odt"), "soy un modulo de texto, saludos", 8)
    val moduloDeVideo = Modulo(listOf("avi","3gp","mkv","mpeg"), "soy un modulo de video, ! saludos?", 20)
    val analizadorDeEstadisticas = Estadisticas()

    servidorWeb.agregarModulo(moduloDeTexto)
    servidorWeb.agregarModulo(moduloDeVideo)
    servidorWeb.agregarModulo(moduloDeImagen)
    servidorWeb.agregarAnalizador(analizadorDeEstadisticas)

    servidorWeb.atenderPedido(unPedidoConImagen)
    servidorWeb.atenderPedido(otroPedidoConImagen)
    servidorWeb.atenderPedido(tercerPedidoConImagen)
    servidorWeb.atenderPedido(otroPedidoConTexto)
    servidorWeb.atenderPedido(unPedidoDeVideo)
    servidorWeb.atenderPedido(otroPedidoDeVideo)
    servidorWeb.atenderPedido(tercerPedidoDeVideo)
    servidorWeb.atenderPedido(cuartoPedidoDeVideo)




    it("tiempo de respuesta promedio"){
      analizadorDeEstadisticas.tiempoDeRespuestaPromedio().shouldBe(29)
    }

    it("cantidad De Pedidos entre dos momentos -fecha/hora- que fueron atendidos"){
      analizadorDeEstadisticas.cantidadDePedidosEntreDosMomentos(LocalDateTime.of(2021, Month.JUNE,10,23,12), LocalDateTime.now()).shouldBe(5)
    }

    it("cantidad de respuestas cuyo body incluye determinado string"){
      analizadorDeEstadisticas.respuestasConBodyTipo("saludos").shouldBe(5)
    }

    



  }


})
