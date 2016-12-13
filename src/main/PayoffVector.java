package main;

import java.util.ArrayList;

public class PayoffVector {
	ArrayList<Double> payoffs;
	
	public PayoffVector(ArrayList<Double> payoffs)
	{
		this.payoffs = payoffs;
	}
	
	public PayoffVector(int num)
	{
		payoffs = new ArrayList<Double>(num);
		for(int i = 0; i < num; i++)
			payoffs.add(0.0);
	}
	
	public void add(PayoffVector other)
	{
		if(payoffs.size() != other.payoffs.size())
			throw new RuntimeException("payoff lengths must match (" + payoffs.size() + ", " + other.payoffs.size() + ")");
		
		for(int i = 0; i < payoffs.size();i++)
		{
			payoffs.set(i, payoffs.get(i) + other.payoffs.get(i));
		}
	}
	
	public void div(double val)
	{
		for(int i = 0; i < payoffs.size();i++)
		{
			payoffs.set(i, payoffs.get(i)/val);
		}
	}
	
}
