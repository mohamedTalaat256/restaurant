import { AuthGuardData, createAuthGuard } from 'keycloak-angular';
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { inject } from '@angular/core';

/**
 * The logic below is a simple example, please make it more robust when implementing in your application.
 *
 * Reason: isAccessGranted is not validating the resource, since it is merging all roles. Two resources might
 * have the same role name and it makes sense to validate it more granular.
 */
const isAccessAllowed = async (
  route: ActivatedRouteSnapshot,
  __: RouterStateSnapshot,
  authData: AuthGuardData
): Promise<boolean | UrlTree> => {
  const { authenticated, grantedRoles } = authData;

  const requiredRole = route.data['role'];
  if (!requiredRole) {
    return false;
  }

  const hasRequiredRole = (role: string): boolean =>
    Object.values(grantedRoles.resourceRoles).some((roles) => roles.includes(role));

  if (authenticated && hasRequiredRole(requiredRole)) {
    return true;
  }

  const router = inject(Router);
  return router.parseUrl('/forbidden');
};

export const canActivateAuthRole = createAuthGuard<CanActivateFn>(isAccessAllowed);


/*

create a componet for product categories the code base and structure should be similar to the products component,
it should have a table to display the categories and a form to add/edit categories, the category should have a name and a description,
also add a column for actions to edit and delete categories, make sure to handle the image upload for the category as well.


and this is category dto

public class CategoryDto {
    private Long id;
    private String name;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}





*/
