package app;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryStack.*;

public class SoundSource {
    IntBuffer channelsBuffer;
    IntBuffer sampleRateBuffer;
    ShortBuffer rawAudioBuffer ;
    int bufferPointer;
    int sourcePointer;

    public void LoadOgg(String file) throws IOException{
        //Allocate space to store return information from the function
        stackPush();
        channelsBuffer = stackMallocInt(1);
        stackPush();
        sampleRateBuffer = stackMallocInt(1);
        rawAudioBuffer = stb_vorbis_decode_filename(file, channelsBuffer, sampleRateBuffer);

        //Retreive the extra information that was stored in the buffers by the function
        int channels = channelsBuffer.get();
        int sampleRate = sampleRateBuffer.get();
        //Free the space we allocated earlier
        stackPop();
        stackPop();

        //Find the correct OpenAL format
        int format = -1;
        if(channels == 1) {
            format = AL_FORMAT_MONO16;
        } else if(channels == 2) {
            format = AL_FORMAT_STEREO16;
        }

        //Request space for the buffer
        bufferPointer = alGenBuffers();

        //Send the data to OpenAL
        alBufferData(bufferPointer, format, rawAudioBuffer, sampleRate);

        //Free the memory allocated by STB
        rawAudioBuffer.clear();
        //Request a source
        sourcePointer = alGenSources();

        //Assign the sound we just loaded to the source
        alSourcei(sourcePointer, AL_BUFFER, bufferPointer);
        alSourcei(sourcePointer, AL_LOOPING,  AL_TRUE  );
        alSourcef(sourcePointer, AL_PITCH,    1.0f          );
        alSourcef(sourcePointer, AL_GAIN,     0.6f          );
    }

    public void clear(){
        alDeleteSources(sourcePointer);
        alDeleteBuffers(bufferPointer);
    }
    public void play(){
        alSourcePlay(sourcePointer);
    }
    public void pause(){
        alSourcePause(sourcePointer);
    }
    public void stop(){
        alSourceStop(sourcePointer);
    }
}
