package mcp.mobius.opis.data.holders.stats;

import io.netty.buffer.ByteBuf;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public abstract class StatAbstract implements Comparable, ISerializable {

    protected DescriptiveStatistics dstat = new DescriptiveStatistics();
    public Long dataPoints = 0L;
    protected Double geomMean = null;
    protected Double dataSum = null;
    protected CoordinatesBlock coord = CoordinatesBlock.INVALID;
    protected CoordinatesChunk chunk = CoordinatesChunk.INVALID;
    protected String name;

	/*
	public StatAbstract(){
		dstat = new DescriptiveStatistics();
	}

	public StatAbstract(int points){
		dstat = new DescriptiveStatistics(points);
	}
	*/

    public void addMeasure(long timing) {
        //this.dstat.addValue((double)timing/1000.0);
        dstat.addValue((double) timing / 1000.);
        dataPoints += 1;
    }

    public void addMeasure(double timing) {
        dstat.addValue(timing);
        dataPoints += 1;
    }

    public double getGeometricMean() {
        if (geomMean != null) {
            return geomMean;
        } else {
            return dstat.getGeometricMean();
        }
    }

    public void setGeometricMean(double value) {
        geomMean = value;
    }

    public double getDataSum() {
        if (dataSum != null) {
            return dataSum;
        } else {
            return dstat.getSum();
        }
    }

    public void setDataSum(double value) {
        dataSum = value;
    }

    public long getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(long ndata) {
        dataPoints = ndata;
    }

    @Override
    public int compareTo(Object o) {
        double value = ((StatAbstract) o).getGeometricMean() - getGeometricMean();
        if (value > 0) {
            return 1;
        }
        if (value < 0) {
            return -1;
        }
        return 0;
    }

    public String getName() {
        return name;
    }

    public CoordinatesBlock getCoordinates() {
        return coord;
    }

    public CoordinatesChunk getChunk() {
        return chunk;
    }

    public CoordinatesBlock getTeleportTarget() {
        return coord;
    }

    public abstract void writeToStream(ByteBuf stream);

}
