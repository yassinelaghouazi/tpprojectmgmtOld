package ma.jconsulting.applications.btpproject.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ma.jconsulting.applications.btpproject.web.rest.TestUtil;

public class CautionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Caution.class);
        Caution caution1 = new Caution();
        caution1.setId(1L);
        Caution caution2 = new Caution();
        caution2.setId(caution1.getId());
        assertThat(caution1).isEqualTo(caution2);
        caution2.setId(2L);
        assertThat(caution1).isNotEqualTo(caution2);
        caution1.setId(null);
        assertThat(caution1).isNotEqualTo(caution2);
    }
}
