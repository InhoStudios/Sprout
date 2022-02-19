package com.joinsdn.sprout.profile;

public class Profile{

    public static void Main(String[] args){



    }

    final double[] ExpectedSpread = {0, 0, 0, 0, 0, 1, 1.5, 2, 2, 1.5, 2, 1, 2, 2, 1.5, 2, 1.5, 2.5, 2, 2, 1.5, 1.5, 1.5, 1.5, 1.5};
    //index i is the default/expected spread on question i + 1, first 5 are zero as those are the veto questions
    final double[] QuestionWeight = {0, 0, 0, 0, 0, 1, 0.5, 0.5, 0.5, 0.5, 1.5, 1.5, 1, 1, 2, 0.5, 1.5, 1, 1.5, 1.5, 1.5, 1.5, 1.5, 2, 2};
    //index i is the weight for question i + 1, first 5 are zero as those are the veto questions

    private double matchQuant = 1.0;
    //this is a scaler for how good of a match the user wants, must be positive, higher = looser match

    private int[] answers = new int[25];
    //index i is the answer to the questions i+1
    public int[] getAnswer(){
        return this.answers;
    }

    private String Instagram = "";
    private String Discord = "";
    private String Snapchat = "";
    private String Phone = "";
    public String[] getSocials(){
        String[] socials = new String[4];
        socials[0] = this.Instagram;
        socials[1] = this.Discord;
        socials[2] = this.Snapchat;
        socials[3] = this.Phone;
        return socials;
    }

    public void setInputs(int[] inputs){
        for(int i = 0 ; i < answers.length; i++){
            this.answers[i] = inputs[i];
        }
    }
    public void setMatchQuant(double match){
        this.matchQuant = match;
    }
    public void setInstagram(String insta){
        this.Instagram = insta;
    }
    public void setDiscord(String disc){
        this.Discord = disc;
    }
    public void setSnapchat(String snap){
        this.Snapchat = snap;
    }
    public void setPhone(String number){
        this.Phone = number;
    }

    public double ProfileMatch(Profile other){
        double sum = 0;
        if(!(other.getAnswer()[0] <= this.answers[2] && other.getAnswer()[0] >= this.answers[1])){
            sum -= 1000000;
        }
        if(this.answers[4] == 1 && (other.getAnswer()[3] == this.answers[3])){
            sum -= 1000000;
        } else if(this.answers[4] == 2 && (other.getAnswer()[3] != this.answers[3])){
            sum -= 1000000;
        }
        for(int i = 5; i < this.answers.length; i++){
            sum += QuestionWeight[i] * (Math.abs(this.answers[i] - other.getAnswer()[i]) - ExpectedSpread[i] * this.matchQuant);
        }
        return sum;
    }

    public boolean IsMatch(Profile other){
        if(ProfileMatch(other) >= 0){
            return true;
        }
        return false;
    }

}