/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capsule;

public interface CapsuleAPI {
    static final int LOG_NONE = 0;
    static final int LOG_QUIET = 1;
    static final int LOG_VERBOSE = 2;
    static final int LOG_DEBUG = 3;
    
    void log1(int level, String str);
}
