/**
* Class simply used to get shot table values
*/
public class ShotTable 
{
	public static int getNumOfPellets(int shotSizeIndex, int ounceIndex)
	{
		switch(shotSizeIndex)
		{
		//2
		case 1:
			switch(ounceIndex)
			{
			//1
			case 3:
				return 90;
			//1 1/8
			case 4:
				return 102;
			//1 1/4
			case 5:
				return 112;
			}
			break;
		//4
		case 2:
			switch(ounceIndex)
			{
			//1/2
			case 0:
				return 68;
			//3/4
			case 1:
				return 101;
			//7/8
			case 2:
				return 118;
			//1
			case 3:
				return 135;
			//1 1/8
			case 4:
				return 152;
			//1 1/4
			case 5:
				return 169;
			}
			break;
		//5
		case 3:
			switch(ounceIndex)
			{
			//1/2
			case 0:
				return 85;
			//3/4
			case 1:
				return 128;
			//7/8
			case 2:
				return 149;
			//1
			case 3:
				return 170;
			//1 1/8
			case 4:
				return 191;
			//1 1/4
			case 5:
				return 213;
			}
			break;
		//6
		case 4:
			switch(ounceIndex)
			{
			//1/2
			case 0:
				return 113;
			//3/4
			case 1:
				return 169;
			//7/8
			case 2:
				return 197;
			//1
			case 3:
				return 225;
			//1 1/8
			case 4:
				return 254;
			//1 1/4
			case 5:
				return 281;
			}
			break;
		//7.5
		case 5:
			switch(ounceIndex)
			{
			//1/2
			case 0:
				return 175;
			//3/4
			case 1:
				return 263;
			//7/8
			case 2:
				return 280;
			//1
			case 3:
				return 350;
			//1 1/8
			case 4:
				return 394;
			//1 1/4
			case 5:
				return 438;
			}
			break;
		//8
		case 6:
			switch(ounceIndex)
			{
			//1/2
			case 0:
				return 211;
			//3/4
			case 1:
				return 308;
			//7/8
			case 2:
				return 359;
			//1
			case 3:
				return 410;
			//1 1/8
			case 4:
				return 462;
			//1 1/4
			case 5:
				return 512;
			}
			break;
		//8.5
		case 7:
			switch(ounceIndex)
			{
			//1/2
			case 0:
				return 240;
			//3/4
			case 1:
				return 373;
			//7/8
			case 2:
				return 419;
			//1
			case 3:
				return 472;
			//1 1/8
			case 4:
				return 556;
			}
			break;
		//9
		case 8:
			switch(ounceIndex)
			{
			//1/2
			case 0:
				return 292;
			//3/4
			case 1:
				return 439;
			//7/8
			case 2:
				return 512;
			//1
			case 3:
				return 585;
			//1 1/8
			case 4:
				return 658;
			//1 1/4
			case 5:
				return 731;
			}
			break;
		}
		return 0;
	}
}
