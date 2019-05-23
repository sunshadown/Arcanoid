#version 330

in vec2 TexCord;
out vec4 Color;

uniform sampler2D mySampler;
uniform int filtertype;

mat3x3 Filter3x3( const int filtertype);

void main()
{
    Color = vec4( 0.0 );

    mat3x3 myfilter = Filter3x3(filtertype);

    Color += mat3x4( textureOffset( mySampler, TexCord, ivec2( -1, -1 ) ),textureOffset( mySampler, TexCord, ivec2(  0, -1 ) ),textureOffset( mySampler, TexCord, ivec2(  1, -1 ) ) ) * myfilter[0];
    Color += mat3x4( textureOffset( mySampler, TexCord, ivec2( -1,  0 ) ),textureOffset( mySampler, TexCord, ivec2(  0,  0 ) ),textureOffset( mySampler, TexCord, ivec2(  1,  0 ) ) ) * myfilter[1];
    Color += mat3x4( textureOffset( mySampler, TexCord, ivec2( -1,  1 ) ),textureOffset( mySampler, TexCord, ivec2(  0,  1 ) ),textureOffset( mySampler, TexCord, ivec2(  1,  1 ) ) ) * myfilter[2];
}


#define NEUTRAL_FILTER               0  
#define AVERAGE_FILTER               1  
#define LP1_FILTER                   2  
#define LP2_FILTER                   3  
#define LP3_FILTER                   4  
#define GAUSS_FILTER                 5  
#define MEAN_REMOVAL_FILTER          6  
#define HP1_FILTER                   7  
#define HP2_FILTER                   8  
#define HP3_FILTER                   9  
#define HORIZONTAL_FILTER           10  
#define VERTICAL_FILTER             11  
#define HORIZONTAL_VERTICAL_FILTER  12  
#define GRADIENT_EAST_FILTER        13  
#define GRADIENT_SOUTH_EAST_FILTER  14  
#define GRADIENT_SOUTH_FILTER       15  
#define GRADIENT_SOUTH_WEST_FILTER  16  
#define GRADIENT_WEST_FILTER        17  
#define GRADIENT_NORTH_WEST_FILTER  18  
#define GRADIENT_NORTH_FILTER       19  
#define GRADIENT_NORTH_EAST_FILTER  20  
#define EMBOSS_EAST_FILTER          21  
#define EMBOSS_SOUTH_EAST_FILTER    22  
#define EMBOSS_SOUTH_FILTER         23  
#define EMBOSS_SOUTH_WEST_FILTER    24  
#define EMBOSS_WEST_FILTER          25  
#define EMBOSS_NORTH_WEST_FILTER    26  
#define EMBOSS_NORTH_FILTER         27  
#define EMBOSS_NORTH_EAST_FILTER    28  
#define LAPLACIAN_LAPL1_FILTER      29  
#define LAPLACIAN_LAPL2_FILTER      30  
#define LAPLACIAN_LAPL3_FILTER      31  
#define LAPLACIAN_DIAGONAL_FILTER   32  
#define LAPLACIAN_HORIZONTAL_FILTER 33  
#define LAPLACIAN_VERTICAL_FILTER   34  
#define SOBEL_HORIZONTAL_FILTER     35  
#define SOBEL_VERTICAL_FILTER       36  
#define PREWITT_HORIZONTAL_FILTER   37  
#define PREWITT_VERTICAL_FILTER     38  


const mat3x3 filters[39] = mat3x3[39]
(
   
    mat3x3( 0.0, 0.0, 0.0,
            0.0, 1.0, 0.0,
            0.0, 0.0, 0.0 ),

    
    mat3x3( 0.11111111, 0.11111111, 0.111111111,
            0.11111111, 0.11111111, 0.111111111,
            0.11111111, 0.11111111, 0.111111111 ),

    
    mat3x3( 0.1, 0.1, 0.1,
            0.1, 0.2, 0.1,
            0.1, 0.1, 0.1 ),

   
    mat3x3( 0.08333333, 0.08333333, 0.08333333,
            0.08333333, 0.33333333, 0.08333333,
            0.08333333, 0.08333333, 0.08333333 ),

    
    mat3x3( 0.05, 0.05, 0.05,
            0.05, 0.6, 0.05,
            0.05, 0.05, 0.05 ),

    
    mat3x3( 0.0625, 0.125, 0.0625,
            0.125, 0.25, 0.125,
            0.0625, 0.125, 0.0625 ),

    
    mat3x3( -1.0, -1.0, -1.0,
            -1.0,  9.0, -1.0,
            -1.0, -1.0, -1.0 ),

    
    mat3x3( 0.0, -1.0, 0.0,
            -1.0, 5.0, -1.0,
            0.0, -1.0, 0.0 ),

    
    mat3x3( 1.0, -2.0, 1.0,
            -2.0, 5.0, -2.0,
            1.0, -2.0, 1.0 ),

    
    mat3x3( 0.0, -0.0625, 0.0,
            -0.0625, 1.2500, -0.0625,
            0.0, -0.0625, 0.0 ),

    
    mat3x3( 0.0, -1.0, 0.0,
            0.0, 1.0, 0.0,
            0.0, 0.0, 0.0 ),

    
    mat3x3( 0.0, 0.0, 0.0,
            -1.0, 1.0, 0.0,
            0.0, 0.0, 0.0 ),

    
    mat3x3 ( -1.0, 0.0, 0.0,
             0.0, 1.0, 0.0,
             0.0, 0.0, 0.0 ),

    
    mat3x3( -1.0, 1.0, 1.0,
            -1.0, -2.0, 1.0,
            -1.0, 1.0, 1.0 ),

    
    mat3x3( -1.0, -1.0, 1.0,
            -1.0, -2.0, 1.0,
            1.0, 1.0, 1.0 ),

    
    mat3x3( -1.0, -1.0, -1.0,
            1.0, -2.0, 1.0,
            1.0, 1.0, 1.0 ),

    
    mat3x3( 1.0, -1.0, -1.0,
            1.0, -2.0, -1.0,
            1.0, 1.0, 1.0 ),

    
    mat3x3( 1.0, 1.0, -1.0,
            1.0, -2.0, -1.0,
            1.0, 1.0, -1.0 ),

    
    mat3x3( 1.0, 1.0, 1.0,
            1.0, -2.0, -1.0,
            1.0, -1.0, -1.0 ),

    
    mat3x3( 1.0, 1.0, 1.0,
            1.0, -2.0, 1.0,
            -1.0, -1.0, -1.0 ),

    
    mat3x3( 1.0, 1.0, 1.0,
            -1.0, -2.0, 1.0,
            -1.0, -1.0, 1.0 ),

    
    mat3x3( -1.0, 0.0, 1.0,
            -1.0, 1.0, 1.0,
            -1.0, 0.0, 1.0 ),

    
    mat3x3( -1.0, -1.0, 0.0,
            -1.0, 1.0, 1.0,
            0.0, 1.0, 1.0 ),

    
    mat3x3( -1.0, -1.0, -1.0,
            0.0, 1.0, 0.0,
            1.0, 1.0, 1.0 ),

    
    mat3x3( 0.0, -1.0, -1.0,
            1.0, 1.0, -1.0,
            1.0, 1.0, 0.0 ),

    
    mat3x3( 1.0, 0.0, -1.0,
            1.0, 1.0, -1.0,
            1.0, 0.0, -1.0 ),

    
    mat3x3( 1.0, 1.0, 0.0,
            1.0, 1.0, -1.0,
            0.0, -1.0, -1.0 ),

    
    mat3x3( 1.0, 1.0, 1.0,
            0.0, 1.0, 0.0,
            -1.0, -1.0, -1.0 ),

    
    mat3x3( 0.0, 1.0, 1.0,
            -1.0, 1.0, 1.0,
            -1.0, -1.0, 0.0 ),

    
    mat3x3( 0.0, -1.0, 0.0,
            -1.0, 4.0, -1.0,
            0.0, -1.0, 0.0 ),

    
    mat3x3( -1.0, -1.0, -1.0,
            -1.0, 8.0, -1.0,
            -1.0, -1.0, -1.0 ),

    
    mat3x3( 1.0, -2.0, 1.0,
            -2.0, 4.0, -2.0,
            1.0, -2.0, 1.0 ),

    
    mat3x3( -1.0, 0.0, -1.0,
            0.0, 4.0, 0.0,
            -1.0, 0.0, -1.0 ),

    
    mat3x3( 0.0, -1.0, 0.0,
            0.0, 2.0, 0.0,
            0.0, -1.0, 0.0 ),

    
    mat3x3( 0.0, 0.0, 0.0,
            -1.0, 2.0, -1.0,
            0.0, 0.0, 0.0 ),

    
    mat3x3( 1.0, 2.0, 1.0,
            0.0, 0.0, 0.0,
            -1.0, -2.0, -1.0 ),

    
    mat3x3( 1.0, 0.0, -1.0,
            2.0, 0.0, -2.0,
            1.0, 0.0, -1.0 ),

    
    mat3x3( -1.0, -1.0, -1.0,
            0.0, 0.0, 0.0,
            1.0, 1.0, 1.0 ),

    
    mat3x3( 1.0, 0.0, -1.0,
            1.0, 0.0, -1.0,
            1.0, 0.0, -1.0 )
);


mat3x3 Filter3x3( const int value )
{
    if( value >= NEUTRAL_FILTER && value <= PREWITT_VERTICAL_FILTER )
        return filters[value];
    else
        return filters[NEUTRAL_FILTER];
}
