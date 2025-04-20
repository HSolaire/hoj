// 获取用户，获取菜单需要的角色，决定菜单是否显示

import { ROLE_ENUM } from "@/access/roleEnum";

export default function roleCheck(loginUser: any, needRole: string) {
  return loginUser.userRole.includes(needRole ?? ROLE_ENUM.GUEST);
}
