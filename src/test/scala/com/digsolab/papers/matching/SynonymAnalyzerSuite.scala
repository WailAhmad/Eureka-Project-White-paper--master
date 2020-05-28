package com.digsolab.papers.matching

import com.digsolab.papers.util.Romanization
import org.scalatest.FunSuite
import java.io._
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute

class SynonymAnalyzerSuite extends FunSuite {
  test("analyze") {
    val reader = new StringReader(
      """
        |sima, serafima, simona, sim'ka
        |kiselyov, kiselev
      """.stripMargin.trim)

    val analyzer = new SynonymAnalyzer(reader)
    val stream = analyzer.tokenStream("test", new StringReader(Romanization.normalize("Сима")))
    stream.reset()
    val builder = List.newBuilder[String]
    while(stream.incrementToken) {
      builder += stream.getAttribute(classOf[CharTermAttribute]).toString
    }
    val res = builder.result()

    assert(res === List("sima", "serafima", "simona", "sim'ka"))
  }
}
