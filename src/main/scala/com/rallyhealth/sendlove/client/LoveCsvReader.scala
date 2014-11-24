package com.rallyhealth.sendlove.client

import com.github.tototoshi.csv.CSVReader
import com.rallyhealth.sendlove.models.Love
import java.io.File

object LoveCsvReader {
  def read(f: File): Seq[Love] = {
    CSVReader.open(f).iterator.map(line => Love(line(1), line(2), line(3), line(4), line(5))).toSeq
  }
}
