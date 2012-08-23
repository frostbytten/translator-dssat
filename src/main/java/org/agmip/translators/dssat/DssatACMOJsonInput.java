package org.agmip.translators.dssat;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.agmip.util.JSONAdapter;
import static org.agmip.util.MapUtil.getObjectOr;

/**
 * DSSAT ACMO mini json Data I/O API Class
 *
 * @author Meng Zhang
 * @version 1.0
 */
public class DssatACMOJsonInput extends DssatCommonInput {

    /**
     * Constructor with no parameters Set jsonKey as "observed"
     *
     */
    public DssatACMOJsonInput() {
        super();
        jsonKey = "acmo";
    }

    /**
     * DSSAT ACMO mini json Data input method
     *
     * @param brMap The holder for BufferReader objects for all files
     * @return result data holder object
     */
    @Override
    protected ArrayList<LinkedHashMap> readFile(HashMap brMap) throws IOException {

        LinkedHashMap file = readACMOJsonData(brMap);
//        decompressData(file);
        return getObjectOr(file, "data", new ArrayList<LinkedHashMap>());
    }

    /**
     * DSSAT ACMO mini json Data input method for Controller using (return map)
     * will not be compressed)
     *
     * @param brMap The holder for BufferReader objects for all files
     * @return result data holder object
     */
    protected LinkedHashMap readACMOJsonData(HashMap brMap) throws IOException {

        LinkedHashMap file = new LinkedHashMap();
        String line;
        StringBuilder sb = new StringBuilder();
        BufferedReader brJ;
        Object buf;

        buf = brMap.get("ACMO.JSON");

        // If ACMO mini json File is no been found
        if (buf == null) {
            return file;
        } else {
            if (buf instanceof char[]) {
                brJ = new BufferedReader(new CharArrayReader((char[]) buf));
            } else {
                brJ = (BufferedReader) buf;
            }
        }

        while ((line = brJ.readLine()) != null) {
            sb.append(line);
        }
        try {
                file = JSONAdapter.fromJSON(sb.toString());
            } catch (Exception e) {
                return file;
            }
        brJ.close();

        return file;
    }

    /**
     * Set reading flags for title lines (marked with *)
     *
     * @param line the string of reading line
     */
    @Override
    protected void setTitleFlgs(String line) {
        flg[0] = "meta";
        flg[1] = "";
        flg[2] = "data";
    }
}
