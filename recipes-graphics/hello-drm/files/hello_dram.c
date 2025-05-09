#include <fcntl.h>
#include <unistd.h>
#include <xf86drm.h>
#include <xf86drmMode.h>
#include <stdint.h>
#include <stdio.h>
#include <string.h>
#include <sys/mman.h>

#define COLOR 0xFF0000FF // Red in ARGB8888

int main() {
    int fd = open("/dev/dri/card0", O_RDWR);
    if (fd < 0) return -1;

    drmModeRes *res = drmModeGetResources(fd);
    drmModeConnector *conn = NULL;
    drmModeEncoder *enc = NULL;
    uint32_t conn_id = 0;

    for (int i = 0; i < res->count_connectors; ++i) {
        conn = drmModeGetConnector(fd, res->connectors[i]);
        if (conn->connection == DRM_MODE_CONNECTED) {
            conn_id = conn->connector_id;
            break;
        }
        drmModeFreeConnector(conn);
    }
    if (!conn_id) return -1;

    drmModeModeInfo mode = conn->modes[0];
    enc = drmModeGetEncoder(fd, conn->encoder_id);

    struct drm_mode_create_dumb creq = {
        .width = mode.hdisplay,
        .height = mode.vdisplay,
        .bpp = 32
    };
    ioctl(fd, DRM_IOCTL_MODE_CREATE_DUMB, &creq);

    struct drm_mode_map_dumb mreq = {
        .handle = creq.handle
    };
    ioctl(fd, DRM_IOCTL_MODE_MAP_DUMB, &mreq);

    uint8_t *map = mmap(0, creq.size, PROT_READ | PROT_WRITE, MAP_SHARED, fd, mreq.offset);
    memset(map, COLOR, creq.size);

    struct drm_mode_fb_cmd fb = {
        .width = creq.width,
        .height = creq.height,
        .pitch = creq.pitch,
        .bpp = 32,
        .depth = 24,
        .handle = creq.handle
    };
    ioctl(fd, DRM_IOCTL_MODE_ADDFB, &fb);

    drmModeSetCrtc(fd, enc->crtc_id, fb.fb_id, 0, 0, &conn_id, 1, &mode);

    sleep(10);

    munmap(map, creq.size);
    close(fd);
    return 0;
}
