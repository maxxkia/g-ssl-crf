package junto.config

import com.typesafe.scalalogging.log4j.Logging
import java.util.Hashtable

/**
 * Created with IntelliJ IDEA.
 * User: masouD
 * Date: 1/9/14
 * Time: 9:11 PM
 * To change this template use File | Settings | File Templates.
 */

object initialScoresConfigLoader extends Logging {
  def apply (config: Hashtable[String, String]): TraversableOnce[Label] = {
    logger.info("Going to read estimated scores graph ...")

    val type2probKey = "type2probability_file"

    val type2Probabilities = {
      if (config.containsKey(type2probKey))
        ((config.get(type2probKey) split(",") map (LabelFileReader(_)) toList) flatten)
      else
        null

    }

    type2Probabilities
  }
}