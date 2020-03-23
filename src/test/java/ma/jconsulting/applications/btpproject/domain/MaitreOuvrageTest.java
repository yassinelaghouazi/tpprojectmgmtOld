package ma.jconsulting.applications.btpproject.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ma.jconsulting.applications.btpproject.web.rest.TestUtil;

public class MaitreOuvrageTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaitreOuvrage.class);
        MaitreOuvrage maitreOuvrage1 = new MaitreOuvrage();
        maitreOuvrage1.setId(1L);
        MaitreOuvrage maitreOuvrage2 = new MaitreOuvrage();
        maitreOuvrage2.setId(maitreOuvrage1.getId());
        assertThat(maitreOuvrage1).isEqualTo(maitreOuvrage2);
        maitreOuvrage2.setId(2L);
        assertThat(maitreOuvrage1).isNotEqualTo(maitreOuvrage2);
        maitreOuvrage1.setId(null);
        assertThat(maitreOuvrage1).isNotEqualTo(maitreOuvrage2);
    }
}
