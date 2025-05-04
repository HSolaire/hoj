// 获取用户，获取菜单需要的角色，决定菜单是否显示

import { ROLE_ENUM } from "@/access/roleEnum";

export default function roleCheck(loginUser: any, needRole: string) {
  return loginUser.userRole.includes(needRole ?? ROLE_ENUM.GUEST);
}

export function roleGetAll(role: string) {
  if (role === ROLE_ENUM.ADMIN) {
    return [ROLE_ENUM.ADMIN, ROLE_ENUM.USER, ROLE_ENUM.GUEST];
  } else if (role === ROLE_ENUM.GUEST) {
    return [ROLE_ENUM.USER, ROLE_ENUM.GUEST];
  } else {
    return [ROLE_ENUM.GUEST];
  }
}
