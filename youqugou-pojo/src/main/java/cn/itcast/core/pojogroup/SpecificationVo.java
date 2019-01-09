package cn.itcast.core.pojogroup;

import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.pojo.specification.SpecificationOption;

import java.io.Serializable;
import java.util.List;

public class SpecificationVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Specification specification;
    private List<SpecificationOption> specificationOptions;

    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public List<SpecificationOption> getSpecificationOptions() {
        return specificationOptions;
    }

    public void setSpecificationOptions(List<SpecificationOption> specificationOptions) {
        this.specificationOptions = specificationOptions;
    }
}
