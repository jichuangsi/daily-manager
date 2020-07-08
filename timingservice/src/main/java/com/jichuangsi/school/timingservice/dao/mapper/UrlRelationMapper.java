package com.jichuangsi.school.timingservice.dao.mapper;

import com.jichuangsi.school.timingservice.model.DeptModel;
import com.jichuangsi.school.timingservice.model.RoleUrlModel;
import com.jichuangsi.school.timingservice.model.RoleUrlUseWayModel;
import com.jichuangsi.school.timingservice.model.UrlMapping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Mapper
public interface UrlRelationMapper {
    @Select("<script>select u.id as id,u.rid as roleId,r.id as urlId,r.`name` as name,r.url as url from urlrelation as u INNER JOIN roleurl as r ON u.uid=r.id where u.rid=#{roleId}</script>")
    List<RoleUrlModel> getRoleUrlByRoleId(String roleId);

    @Select("<script>SELECT ru.id as id,ru.`name`as`name`,ru.url as url,sp.id as usewayid,sp.`name`as usewayname FROM staticpage_url su INNER JOIN  roleurl ru ON su.url_id=ru.id INNER JOIN static_page sp ON su.static_page_id=sp.id</script>")
    List<RoleUrlUseWayModel> getAllRoleUrl();

    //查询角色可以访问的模块和静态页面
    @Select("<script>SELECT u.id as modelId,u.`name` as modelName,sp.id as staticPageId,sp.`name` as staticPageName,sp.static_url as staticPageUrl FROM resource_staticpage as rs INNER JOIN useway u ON rs.resource_id=u.id INNER JOIN static_page sp ON rs.static_page_id=sp.id WHERE static_page_id in(" +
            "SELECT static_page_id FROM staticpage_url WHERE url_id in(" +
            "SELECT uid FROM urlrelation WHERE rid =#{roleId})" +
            ")</script>")
    List<UrlMapping> getModelAndStaticPageByRoleId(String roleId);

    //查询角色可以访问的静态页面和url
    @Select("SELECT u.id as relationId,sp.id as staticPageId,sp.`name` as staticPageName,sp.static_url as staticPageUrl,ru.id as id,ru.`name` as name ,ru.url as roleUrl FROM staticpage_url as su INNER JOIN static_page as sp ON su.static_page_id=sp.id INNER JOIN roleurl as ru ON su.url_id=ru.id INNER JOIN urlrelation u ON ru.id=u.uid  WHERE url_id in(SELECT uid FROM urlrelation WHERE rid =#{roleId}) AND sp.id=#{pageId}")
    List<UrlMapping> getStaticPageAndRoleUrlByRoleIdAndPageId(String roleId,String pageId);

    @Select("SELECT u.id as relationId,sp.id as staticPageId,sp.`name` as staticPageName,sp.static_url as staticPageUrl,ru.id as id,ru.`name` as name ,ru.url as roleUrl  FROM staticpage_url as su INNER JOIN static_page as sp ON su.static_page_id=sp.id INNER JOIN roleurl as ru ON su.url_id=ru.id INNER JOIN urlrelation u ON ru.id=u.uid  WHERE url_id in(SELECT uid FROM urlrelation WHERE rid =#{roleId}) AND u.rid=#{roleId}")
    List<UrlMapping> getStaticPageAndRoleUrlByRoleId(String roleId);

    @Select("<script>SELECT r.id as id ,r.dept_id as deptId,d.deptname as deptName,r.role_id as roleId from role_department r INNER JOIN department d ON r.dept_id=d.id where r.role_id=#{roleId}</script>")
    List<DeptModel> findByRoleId(String roleId);
}
