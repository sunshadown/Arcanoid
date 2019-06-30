package app;

public class MYFilters {
            public final int NEUTRAL_FILTER              = 0;
            public final int AVERAGE_FILTER              = 1;
            public final int LP1_FILTER                  = 2;
            public final int LP2_FILTER                  = 3;
            public final int LP3_FILTER                  = 4;
            public final int GAUSS_FILTER                = 5;
            public final int MEAN_REMOVAL_FILTER         = 6;
            public final int HP1_FILTER                  = 7;
            public final int HP2_FILTER                  = 8;
            public final int HP3_FILTER                  = 9;
            public final int HORIZONTAL_FILTER           =10;
            public final int VERTICAL_FILTER             =11;
            public final int HORIZONTAL_VERTICAL_FILTER  =12;
            public final int GRADIENT_EAST_FILTER        =13;
            public final int GRADIENT_SOUTH_EAST_FILTER  =14;
            public final int GRADIENT_SOUTH_FILTER       = 15;
            public final int GRADIENT_SOUTH_WEST_FILTER  = 16;
            public final int GRADIENT_WEST_FILTER        = 17;
            public final int GRADIENT_NORTH_WEST_FILTER  = 18;
            public final int GRADIENT_NORTH_FILTER       = 19;
            public final int GRADIENT_NORTH_EAST_FILTER  = 20;
            public final int EMBOSS_EAST_FILTER          = 21;
            public final int EMBOSS_SOUTH_EAST_FILTER    = 22;
            public final int EMBOSS_SOUTH_FILTER         = 23;
            public final int EMBOSS_SOUTH_WEST_FILTER    = 24;
            public final int EMBOSS_WEST_FILTER          = 25;
            public final int EMBOSS_NORTH_WEST_FILTER    = 26;
            public final int EMBOSS_NORTH_FILTER         = 27;
            public final int EMBOSS_NORTH_EAST_FILTER    = 28;
            public final int LAPLACIAN_LAPL1_FILTER      = 29;
            public final int LAPLACIAN_LAPL2_FILTER      = 30;
            public final int LAPLACIAN_LAPL3_FILTER      = 31;
            public final int LAPLACIAN_DIAGONAL_FILTER   = 32;
            public final int LAPLACIAN_HORIZONTAL_FILTER = 33;
            public final int LAPLACIAN_VERTICAL_FILTER   = 34;
            public final int SOBEL_HORIZONTAL_FILTER     = 35;
            public final int SOBEL_VERTICAL_FILTER       = 36;
            public final int PREWITT_HORIZONTAL_FILTER   = 37;
            public final int PREWITT_VERTICAL_FILTER     = 38;

            public static final String filterName[] =
            {
                    "filtr neutralny",
                    "filtr u�redniaj�cy",
                    "filtr LP1",
                    "filtr LP2",
                    "filtr LP3",
                    "filtr Gaussa",
                    "filtr usuwaj�cy �redni�",
                    "filtr HP1",
                    "filtr HP2",
                    "filtr HP3",
                    "filtr poziomy",
                    "filtr pionowy",
                    "filtr poziomy/pionowy",
                    "filtr gradientowy wsch�d",
                    "filtr gradientowy po�udniowy wsch�d",
                    "filtr gradientowy po�udnie",
                    "filtr gradientowy po�udniowy zach�d",
                    "filtr gradientowy zach�d",
                    "filtr gradientowy p�nocny zach�d",
                    "filtr gradientowy p�noc",
                    "filtr gradientowy p�nocny wsch�d",
                    "filtr uwypuklaj�cy wsch�d",
                    "filtr uwypuklaj�cy po�udniowy wsch�d",
                    "filtr uwypuklaj�cy po�udnie",
                    "filtr uwypuklaj�cy po�udniowy zach�d",
                    "filtr uwypuklaj�cy zach�d",
                    "filtr uwypuklaj�cy p�nocny zach�d",
                    "filtr uwypuklaj�cy p�noc",
                    "filtr uwypuklaj�cy p�nocny wsch�d",
                    "filtr Laplace'a LAPL1",
                    "filtr Laplace'a LAPL2",
                    "filtr Laplace'a LAPL3",
                    "filtr Laplace'a sko�ny",
                    "filtr Laplace'a poziomy",
                    "filtr Laplace'a pionowy",
                    "filtr poziomy Sobela",
                    "filtr pionowy Sobela",
                    "filtr poziomy Prewitta",
                    "filtr pionowy Prewitta"
            };

}
