package com.jacob.benchmarks

import java.util.concurrent.atomic.AtomicLong

import org.openjdk.jmh.annotations.{Benchmark, BenchmarkMode, Scope, State}
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.infra.Blackhole
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.parquet.hadoop.metadata.CompressionCodecName
import org.apache.spark.sql.types.DataTypes._
import org.apache.spark.sql.types.{StructField, StructType}
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.internal.SQLConf
import org.apache.parquet.hadoop.ParquetOutputFormat
import org.apache.spark.unsafe.types.UTF8String


@State(Scope.Thread)
class ParquetBenchmark {

  private val value = new AtomicLong()

  def getConf(schema: StructType): Configuration = {
    val conf = new Configuration()
    conf.set(ParquetOutputFormat.WRITE_SUPPORT_CLASS, "org.apache.spark.sql.execution.datasources.parquet.ParquetWriteSupport")
//    conf.set(ParquetOutputFormat.BLOCK_SIZE, "")
//    conf.set(ParquetOutputFormat.PAGE_SIZE, "")
    conf.set("org.apache.spark.sql.parquet.row.attributes", schema.json)
    conf.set(SQLConf.PARQUET_WRITE_LEGACY_FORMAT.key, "false")
    conf.set(SQLConf.PARQUET_OUTPUT_TIMESTAMP_TYPE.key, SQLConf.ParquetOutputTimestampType.TIMESTAMP_MILLIS.toString())
    //conf.set(SQLConf.PARQUET_WRITE_LEGACY_FORMAT.key, "false")
    conf
  }

  def getSchema(): StructType = {
    StructType(StructField("a",StringType,false) ::
      StructField("b",StringType,false) ::
      StructField("c",StringType,false) ::
      StructField("d",StringType,false) :: Nil)
  }

  @Benchmark
  @BenchmarkMode(Mode.SingleShotTime)
  def writeParquet10000(bh: Blackhole): Unit = {

    val output = new Path("test-100-%s.parquet".format(System.currentTimeMillis))

    val recordWriter = new ParquetOutputFormat[InternalRow]().getRecordWriter(getConf(getSchema()), output, CompressionCodecName.GZIP)

    (1 to 100000).foreach(_ => {
      val row = InternalRow.fromSeq(Seq(UTF8String.fromString("this"),
        UTF8String.fromString("isway"),
        UTF8String.fromString("betterthan"),
        UTF8String.fromString("usingJAva")))
      recordWriter.write(null, row)
    })

    //recordWriter.
  }

}
