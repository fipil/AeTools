package com.spincoders.aetools.jobs.regions;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Region {

    public int x;
    public int z;

    private final int REGION_SIZE=512;

    private boolean invalid;

    public Region(File regFile) {
        Pattern pattern = Pattern.compile("^r\\.(-?[0-9]+)\\.(-?[0-9]+)\\.mca$");
        Matcher matcher = pattern.matcher(regFile.getName());

        if (matcher.matches())
        {
            x = Integer.valueOf(matcher.group(1));
            z = Integer.valueOf(matcher.group(2));
        } else
            invalid=true;
    }

    public boolean isValid() {
        return !invalid;
    }

    public int minPosX() {
        return x*REGION_SIZE;
    }
    public int minPosZ() {
        return z*REGION_SIZE;
    }
    public int maxPosX() {
        return minPosX()+REGION_SIZE-1;
    }
    public int maxPosZ() {
        return minPosZ()+REGION_SIZE-1;
    }

    @Override
    public String toString() {
        return "Region["+x+","+z+"] at x from "+minPosX()+" to "+maxPosX()+", z from "+minPosZ()+" to "+maxPosZ();
    }

    public String name() {
        return "Region ["+x+","+z+"]";
    }
}
