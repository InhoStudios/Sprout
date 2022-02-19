package com.joinsdn.sprout.profile;

public class ProfileObject {

    public static void Main(String[] args){

        double[] ExpectedSpread = {0, 0, 0, 0, 0, 1, 1.5, 2, 2, 1.5, 2, 1, 2, 2, 1.5, 2, 1.5, 2.5, 2, 2, 1.5, 1.5, 1.5, 1.5, 1.5};
        //index i is the default/expected spread on question i + 1, first 5 are zero as those are the veto questions
        double[] QuestionWeight = {0, 0, 0, 0, 0, 1, 0.5, 0.5, 0.5, 0.5, 1.5, 1.5, 1, 1, 2, 0.5, 1.5, 1, 1.5, 1.5, 1.5, 1.5, 1.5, 2, 2};
        //index i is the weight for question i + 1, first 5 are zero as those are the veto questions

    }

}

class Profile{

    private int[] answers = new int[25];
    //index i is the answer to the questions i+1
    private double matchQuant = 1.0;
    //this is a scaler for how good of a match the user wants, must be positive, higher = looser match

    public void changeBoth(int[] inputs, double match){
        for(int i = 0; i < answers.length; i++){
            this.answers[i] = inputs[i];
        }
        this.matchQuant = match;
    }

    public void changeInputs(int[] inputs){
        for(int i = 0; i < answers.length; i++){
            this.answers[i] = inputs[i];
        }
    }

    public void changeMatchQuant(double match){
        this.matchQuant = match;
    }

    public double ProfileMatch(Profile other, double[] ExpectedSpread, double[] QuestionWeight){
        double sum = 0;
        if(!(other.answers[0] <= this.answers[2] && other.answers[0] >= this.answers[1])){
            sum -= 1000000;
        }

        //still need to handle question 4/5

        for(int i = 5; i < this.answers.length; i++){
            sum += QuestionWeight[i]*(Math.log(Math.pow(0.5,(Math.abs(this.answers[i] - other.answers[i])) - ExpectedSpread[i] * this.matchQuant)));
        }
        return sum;
    }

    public boolean IsMatch(Profile other, double[] ExpectedSpread, double[] QuestionWeight){
        if(ProfileMatch(other, ExpectedSpread, QuestionWeight) >= 0){
            return true;
        }
        return false;
    }

}