package cn.deepclue.datamaster.cleaner.domain.vo.workspace.cleaning;

import cn.deepclue.datamaster.cleaner.domain.po.cleaning.TransformationPO;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;
import cn.deepclue.datamaster.streamer.transform.Transformation;
import cn.deepclue.datamaster.streamer.transform.transformer.TransformerDef;
import cn.deepclue.datamaster.streamer.util.JsonUtils;
import com.google.common.reflect.TypeToken;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuzb on 18/04/2017.
 */
public class TransformationVO {
    private Integer tfid;
    private String tftype;
    private Integer wsid;
    private Integer wsversion;
    private Map<String, Object> args;
    private List<FilterVO> filters;
    private TransformerDef transformerDef;

    public Integer getTfid() {
        return tfid;
    }

    public void setTfid(Integer tfid) {
        this.tfid = tfid;
    }

    public String getTftype() {
        return tftype;
    }

    public void setTftype(String tftype) {
        this.tftype = tftype;
    }

    public Integer getWsid() {
        return wsid;
    }

    public void setWsid(Integer wsid) {
        this.wsid = wsid;
    }

    public Integer getWsversion() {
        return wsversion;
    }

    public void setWsversion(Integer wsversion) {
        this.wsversion = wsversion;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }

    public Object getArg(String name) {
        return args.get(name);
    }

    public void addArg(String key, Object value) {
        if (args == null) {
            args = new HashMap<>();
        }

        args.put(key, value);
    }

    public List<FilterVO> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterVO> filters) {
        this.filters = filters;
    }

    public TransformerDef getTransformerDef() {
        return transformerDef;
    }

    public static TransformationPO toPO(TransformationVO transformationVO) {
        TransformationPO transformationPO = new TransformationPO();
        BeanUtils.copyProperties(transformationVO, transformationPO);
        transformationPO.setArgs(JsonUtils.toJson(transformationVO.args));

        if (transformationVO.filters != null) {
            transformationPO.setFilters(JsonUtils.toJson(transformationVO.filters));
        }

        return transformationPO;
    }

    public static Transformation toBO(TransformationVO transformationVO) {
        Transformation transformation = new Transformation();
        BeanUtils.copyProperties(transformationVO, transformation);
        transformation.setArgs(transformationVO.args);

        if (transformationVO.filters != null) {
            transformation.setFilters(FilterVO.toFilters(transformationVO.filters));
        }

        return transformation;
    }

    public static TransformationVO fromPO(TransformationPO transformationPO) {
        TransformationVO transformationVO = new TransformationVO();
        BeanUtils.copyProperties(transformationPO, transformationVO);

        Type argsType = new TypeToken<HashMap<String, Object>>(){}.getType();
        Map<String, Object> args = JsonUtils.fromJson(transformationPO.getArgs(), argsType);
        transformationVO.setArgs(args);

        // Build filters
        if (transformationPO.getFilters() != null) {
            Type filtersType = new TypeToken<ArrayList<FilterVO>>(){}.getType();
            List<FilterVO> filterVOs = JsonUtils.fromJson(transformationPO.getFilters(), filtersType);
            transformationVO.setFilters(filterVOs);
        }

        // Set transformer def
        transformationVO.transformerDef = TransformHelper.buildTransformerDef(transformationPO.getTftype());

        return transformationVO;
    }

    public static List<Transformation> toBOs(List<TransformationVO> transformationVOs) {
        List<Transformation> transformations = new ArrayList<>();
        for (TransformationVO transformationVO : transformationVOs) {
            transformations.add(toBO(transformationVO));
        }

        return transformations;
    }
}
