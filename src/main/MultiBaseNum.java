package main;

public class MultiBaseNum implements Comparable<MultiBaseNum> {
	private int[] bases;
	private int[] value;
	private int baselessValue;
	private int maxValue;
	
	public MultiBaseNum(MultiBaseNum other)
	{
		maxValue = other.maxValue;
		baselessValue = other.baselessValue;
		bases = new int[other.bases.length];
		value = new int[bases.length];
		
		for(int i = 0; i < bases.length; i++)
		{
			bases[i] = other.bases[i];
			value[i] = other.value[i];	
		}
	}
	
	public MultiBaseNum(int[] bases)
	{
		maxValue = 1;
		baselessValue = 0;
		value = new int[bases.length];
		this.bases = new int[bases.length]; //deep copy
		for(int i = 0; i < value.length; i++)
		{
			if(bases[i] == 0)
				throw new RuntimeException("cannot have base 0");
			this.bases[i] = bases[i];
			maxValue *= bases[i];
			value[i] = 0;
		}
	}
	
	public int[] getValue()
	{
		return value;
		/*int[] ret = new int[value.length];
		for(int i = 0; i < value.length; i++)
		{
			ret[i] = value[i];
		}
		return ret;*/
	}
	
	public void set(int[] value)
	{
		if(value.length != bases.length)
			throw new RuntimeException("Number of bases and positions must match");
		for(int i = 0; i < value.length; i++)
		{
			if(value[i] > bases[i]-1)
				throw new RuntimeException(value[i] + " greater than base " + bases[i]);
		}
		for(int i = 0; i < value.length; i++)
		{
			this.value[i] = value[i];
		}
		updateBaseless(this.value);
	}
	
	private void updateBaseless(int[] value)
	{
		baselessValue = 0;
		for(int i = 0; i < value.length; i++)
		{
			baselessValue = baselessValue * bases[i] + value[i];
		}
	}
	
	public void set(int baselessValue)
	{
		if(baselessValue >= maxValue)
			throw new RuntimeException("integer value too large for bases");
		
		this.baselessValue = baselessValue;
		update(baselessValue);
	}
	
	private void update(int baselessValue)
	{
		for(int i = bases.length - 1; i >= 0; i--)
		{
			value[i] = baselessValue % bases[i];
			baselessValue = baselessValue/bases[i];
		}
	}
	
	public void mul(int other)
	{
		baselessValue *= other;
		update(baselessValue);
	}
	
	public void div(int other)
	{
		baselessValue /= other;
		update(baselessValue);
	}
	
	public void inc()
	{
		baselessValue++;
		update(baselessValue);
	}
	
	public void dec()
	{
		baselessValue--;
		update(baselessValue);
	}
	
	public int getMax()
	{
		return maxValue;
	}
	
	@Override
	public int compareTo(MultiBaseNum other) {
		if(baselessValue < other.baselessValue)
			return -1;
		if(baselessValue > other.baselessValue)
			return 1;
		return 0;
	}
}
