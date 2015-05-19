package com.molamil.utils;

public class MathUtils {
	public static float randomRange(float min, float max)
	{
		return min + (int)(Math.random() * ((max - min) + 1));
	}
}
