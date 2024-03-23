package lucaswolschick.moa_scp_mh.resolvedor;

import java.util.Random;

public class ThreadRandomFactory {
    public static Random getRandom(Long... seq) {
        // hash seed and threadId to get a new seed
        long hash = Long.parseUnsignedLong("14695981039346656037");

        for (long x : seq) {
            for (int i = 0; i < 8; i++) {
                hash ^= (x & 0xff);
                hash *= 1099511628211L;
                x >>= 8;
            }
        }

        return new Random(hash);
    }
}
