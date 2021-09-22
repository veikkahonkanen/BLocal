package com.bteam.blocal.utility;

import com.bteam.blocal.data.model.errors.NoDocumentException;
import com.bteam.blocal.data.model.errors.UnsuccessfulQueryException;

public class ErrorToString {
    public static int convert(Throwable err){
        if(err instanceof UnsuccessfulQueryException){

        }
        else if(err instanceof NoDocumentException){

        }

        return -1;
    }
}
