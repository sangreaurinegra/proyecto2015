/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.proyecto.file;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class KeyFits implements WritableComparable {


    private final Text nombreImg = new Text();


    public void setNombreImg(Text value) {
        nombreImg.set(value);
    }
  

    public Text getNombreImg() {
        return nombreImg;
    }


    @Override
    public void write(DataOutput out) throws IOException {
        System.out.println("WRITE KEY NombreImg - " + nombreImg.toString());
        nombreImg.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        nombreImg.readFields(in);
    }

    @Override
    public int compareTo(Object o) {
        KeyFits sentiment = (KeyFits) o;
        Text thisValue = this.nombreImg;
        Text thatValue = sentiment.nombreImg;

        return this.equals(o) ? 0 : (thatValue.compareTo(thisValue) == 0 ? -1 : 1);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + nombreImg.hashCode();
        //result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KeyFits other = (KeyFits) obj;
        return Objects.equals(this.nombreImg, other.nombreImg);
    }

    @Override
    public String toString() {
        return  nombreImg.toString() ;
    }    
}
