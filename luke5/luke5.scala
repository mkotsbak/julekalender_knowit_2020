import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global

case class Point(x: Int, y: Int)

// Fra Shoelace-formula i rosetta code
case class Polygon( pp:List[Point] ) {
  require( pp.size > 2, "A Polygon must consist of more than two points" )

  override def toString = "Polygon(" + pp.mkString(" ", ", ", " ") + ")"

  def area = {

    // Calculate using the Shoelace Formula
    val xx = pp.map( p => p.x )
    val yy = pp.map( p => p.y )
    val overlace = xx zip yy.drop(1)++yy.take(1)
    val underlace = yy zip xx.drop(1)++xx.take(1)

    (overlace.map( t => t._1 * t._2 ).sum - underlace.map( t => t._1 * t._2 ).sum).abs / 2.0
  }
}

object Luke5 extends App {
  //val input = "HHOOVVNN"
  //val input = "HHHHHHOOOOVVNNNVVOVVNN"
  val input = dom.experimental.Fetch.fetch("https://julekalender-backend.knowit.no/challenges/5/attachments/rute.txt")
    .toFuture.flatMap(_.text().toFuture).foreach { input =>

    val positions = input.scanLeft( Point(0, 0) ) { (curPos, cmd) =>
      cmd match {
        case 'H' => Point(curPos.x + 1, curPos.y)
        case 'V' => Point(curPos.x - 1, curPos.y)
        case 'O' => Point(curPos.x, curPos.y + 1)
        case 'N' => Point(curPos.x, curPos.y - 1)
      }
    }

    println(
      Polygon(positions.toList).area.toInt
    )

    drawPointsToCanvas(positions)
  }

  def drawPointsToCanvas(positions: Seq[Point]) = {
    import dom.html.Canvas

    val canvas: Canvas = dom.document.getElementsByTagName("canvas")(0).asInstanceOf[Canvas]
    val xmin = positions.map(_.x).min
    val ymin = positions.map(_.y).min

    println("Min X: " + xmin)
    println("Min Y: " + ymin)

    val movedPos = positions.map( pt => Point(pt.x + -xmin, pt.y + -ymin) )
    canvas.width = positions.map(_.x).max - xmin
    canvas.height = positions.map(_.y).max - ymin

    val ctx: dom.CanvasRenderingContext2D = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

    ctx.lineWidth = 1
    ctx.beginPath()

    movedPos.foreach(pt => ctx.lineTo(pt.x, pt.y))

    ctx.stroke()
  }
}